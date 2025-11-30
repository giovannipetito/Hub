package it.giovanni.hub.presentation.screen.detail.comfyui

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object ComfyUtils {

    // Builds the body required by ComfyUI for the txt2img workflow
    fun buildTextToImageRequestBody(
        context: Context,
        prompt: String
    ): Pair<JsonObject, String> {

        val workflowJson = getWorkflowJson(context, "txt2img_api.json")

        // overrides the prompt (node "6" in the graph)
        workflowJson["6"].asJsonObject
            .getAsJsonObject("inputs")
            .addProperty("text", prompt)

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
            throw IllegalStateException("No SaveImage node found in txt2img_api.json")
        }

        return JsonObject().apply {
            add("prompt", workflowJson)
        } to saveNodeId
    }

    fun buildHairColorRequestBody(
        context: Context,
        prompt: String,
        uploadedImageName: String?
    ): Pair<JsonObject, String> {

        val workflowJson = getWorkflowJson(context, "img2img_hair_color_api.json")

        // Node 23: CLIPTextEncode positive prompt ("red hair" -> "${selected.name} hair")
        workflowJson.getAsJsonObject("23")
            ?.getAsJsonObject("inputs")
            ?.addProperty("text", prompt)

        // Node 22: LoadImage -> use uploaded image filename from /upload/image
        workflowJson.getAsJsonObject("22")
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

        return JsonObject().apply {
            add("prompt", workflowJson)
        } to saveNodeId
    }

    // Load the local JSON
    private fun getWorkflowJson(context: Context, fileName: String): JsonObject {
        val inputStream: InputStream = context.assets.open(fileName)
        val jsonString: String = inputStream.bufferedReader().use { it.readText() }
        return JsonParser.parseString(jsonString).asJsonObject
    }

    /**
     * Uploads the selected image (gallery/camera Uri) to ComfyUI /upload/image
     * and returns the resulting filename that will be used in the LoadImage node.
     */
    suspend fun uploadImageToComfyUI(
        context: Context,
        baseUrl: String,
        sourceImageUri: Uri
    ): String? = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver

        val mimeType = resolver.getType(sourceImageUri) ?: "image/jpeg"
        val inputStream = resolver.openInputStream(sourceImageUri)
            ?: throw IllegalStateException("Cannot open InputStream for $sourceImageUri")

        val bytes = inputStream.use { it.readBytes() }
        if (bytes.isEmpty()) {
            throw IllegalStateException("Image bytes are empty for $sourceImageUri")
        }

        val fileName = "hair_${System.currentTimeMillis()}.jpg"

        Log.d(
            "ComfyUI",
            "Uploading image: uri=$sourceImageUri, mime=$mimeType, size=${bytes.size} bytes, " +
                    "as fileName=$fileName to baseUrl=$baseUrl"
        )

        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", fileName, requestBody)
            .build()

        val url = baseUrl.trimEnd('/') + "/upload/image"

        val request = Request.Builder()
            .url(url)
            .post(multipartBody)
            .build()

        val client = OkHttpClient.Builder()
            .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val bodyString = response.body.string()
                Log.d(
                    "ComfyUI",
                    "uploadImageToComfy response: code=${response.code}, body=$bodyString"
                )

                if (!response.isSuccessful) {
                    throw IOException("Upload failed: HTTP ${response.code}")
                }

                val json = JSONObject(bodyString)

                val imageObj = if (json.has("images")) {
                    json.getJSONArray("images").getJSONObject(0)
                } else {
                    json
                }

                val uploadedName = imageObj.getString("name")
                Log.d("ComfyUI", "uploadImageToComfy parsed uploadedName=$uploadedName")
                uploadedName
            }
        } catch (e: Exception) {
            Log.e("ComfyUI", "uploadImageToComfyUI error", e)
            throw e
        }
    }
}