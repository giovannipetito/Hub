package it.giovanni.hub.presentation.viewmodel.comfyui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.domain.repositoryint.remote.ComfyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class HairColorViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: ComfyRepository
) : BaseViewModel(context) {

    fun generateImage(
        comfyUrl: String,
        prompt: String,
        sourceImageUri: Uri,
        onResult: (Result<Unit>) -> Unit
    ) = viewModelScope.launch {
        try {
            val baseUrl = comfyUrl.trim()
            require(baseUrl.isNotBlank()) { "Comfy baseUrl is empty" }

            // 1) Upload the selected image (gallery/camera) to ComfyUI
            val uploadedName = uploadImageToComfy(baseUrl, sourceImageUri)
                ?: throw IllegalStateException("Image upload failed")

            // 2) Build workflow body AND discover the SaveImage node id from JSON
            val (body, saveNodeId) = buildHairColorRequestBody(
                prompt = prompt,
                uploadedImageName = uploadedName
            )

            // 3) Start the run
            val startResponse: JsonObject = repository.startRun(body)
            Log.d("ComfyUI", "Hair startRun = $startResponse")

            val promptId = startResponse.get("prompt_id")?.asString
                ?: startResponse.getAsJsonObject("prompt")?.get("id")?.asString
                ?: throw IllegalStateException("promptId not found in startRun response")

            Log.d("ComfyUI", "Hair queued promptId = $promptId, saveNodeId = $saveNodeId")

            // 4) Poll /history/{promptId} until SaveImage node produces output
            val success = withTimeoutOrNull(120_000L) { // 2 minutes
                while (isActive) {
                    val historyRoot: JsonObject = repository.getRun(promptId = promptId)
                    Log.d("ComfyUI", "Hair history($promptId) = $historyRoot")

                    val entry = historyRoot.getAsJsonObject(promptId)
                    if (entry == null) {
                        // history not ready yet
                        delay(1_000)
                        continue
                    }

                    val statusObj = entry.getAsJsonObject("status")
                    val statusStr = statusObj?.get("status")?.asString ?: ""
                    val completedFlag = statusObj?.get("completed")?.takeIf { it.isJsonPrimitive }?.asBoolean ?: false

                    val outputs = entry.getAsJsonObject("outputs")

                    // *** IMPORTANT: read the SaveImage node, not ControlNet ***
                    val saveNode = outputs?.getAsJsonObject(saveNodeId)
                    val imagesArray = saveNode?.getAsJsonArray("images")
                    val firstImage = imagesArray?.firstOrNull()?.asJsonObject

                    if (firstImage != null) {
                        val filename = firstImage["filename"].asString
                        val subfolder = firstImage["subfolder"].asString
                        val type = firstImage["type"].asString  // usually "output" or "temp"

                        val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                        val encodedSubfolder = URLEncoder.encode(subfolder, StandardCharsets.UTF_8.toString())

                        val base = comfyUrl.let {
                            if (it.endsWith("/")) it else "$it/"
                        }

                        val url = "${base}view?filename=$encodedFilename&subfolder=$encodedSubfolder&type=$type"

                        Log.d("ComfyUI", "Hair final Image URL = $url")

                        imageUrl = url
                        notifyImageReady(url)
                        onResult(Result.success(Unit))
                        return@withTimeoutOrNull true
                    }

                    // If run is done but SaveImage never produced images, stop.
                    if (completedFlag || statusStr == "error" || statusStr == "completed") {
                        Log.w(
                            "ComfyUI",
                            "HairColor: run finished (status=$statusStr, completed=$completedFlag) " +
                                    "but SaveImage node $saveNodeId has no images."
                        )
                        return@withTimeoutOrNull false
                    }

                    // Still running; keep polling
                    delay(1_000)
                }
                false
            } ?: false

            if (!success) {
                Log.e("ComfyUI", "HairColor: timed out or failed to get image from SaveImage node")
                onResult(Result.failure(Exception("Timed out or no final image from ComfyUI")))
            }

        } catch (e: Exception) {
            Log.e("ComfyUI", "Error generating hair color image", e)
            onResult(Result.failure(e))
        }
    }

    /**
     * Uploads the selected image (gallery/camera Uri) to ComfyUI /upload/image
     * and returns the resulting filename that will be used in the LoadImage node.
     */
    private suspend fun uploadImageToComfy(
        baseUrl: String,
        sourceImageUri: Uri
    ): String? = withContext(Dispatchers.IO) {
        runCatching {
            val resolver = context.contentResolver

            val mimeType = resolver.getType(sourceImageUri) ?: "image/jpeg"
            val bytes = resolver.openInputStream(sourceImageUri)?.use { it.readBytes() }
                ?: return@runCatching null

            val fileName = "hair_${System.currentTimeMillis()}.jpg"

            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", fileName, requestBody)
                .build()

            val client = OkHttpClient()
            val url = baseUrl.trimEnd('/') + "/upload/image"

            val request = Request.Builder()
                .url(url)
                .post(multipartBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("ComfyUI", "uploadImageToComfy failed: HTTP ${response.code}")
                    return@runCatching null
                }

                val bodyString = response.body?.string() ?: return@runCatching null
                Log.d("ComfyUI", "uploadImageToComfy response: $bodyString")

                val json = JSONObject(bodyString)

                val imageObj = if (json.has("images")) {
                    json.getJSONArray("images").getJSONObject(0)
                } else {
                    json
                }

                imageObj.getString("name")
            }
        }.getOrNull()
    }

    private fun buildHairColorRequestBody(
        prompt: String,
        uploadedImageName: String
    ): Pair<JsonObject, String> {

        val jsonStr = context.assets.open("img2img_hair_color_api.json")
            .bufferedReader()
            .use { it.readText() }

        val workflowJson = JsonParser.parseString(jsonStr).asJsonObject

        // Node 30: CLIPTextEncode positive prompt ("red hair" -> "${selected.name} hair")
        workflowJson.getAsJsonObject("30")
            ?.getAsJsonObject("inputs")
            ?.addProperty("text", prompt)

        // Node 29: LoadImage -> use uploaded image filename from /upload/image
        workflowJson.getAsJsonObject("29")
            ?.getAsJsonObject("inputs")
            ?.addProperty("image", uploadedImageName)

        // Find SaveImage node id dynamically (whatever id ComfyUI assigned)
        var saveNodeId: String? = null
        for ((key, value) in workflowJson.entrySet()) {
            val nodeObj = value.asJsonObject
            if (nodeObj.get("class_type")?.asString == "SaveImage") {
                saveNodeId = key
                break
            }
        }

        if (saveNodeId == null) {
            throw IllegalStateException("No SaveImage node found in img2img_hair_color_api.json")
        }

        val body = JsonObject().apply {
            add("prompt", workflowJson)
        }

        return body to saveNodeId
    }
}