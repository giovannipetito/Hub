package it.giovanni.hub.presentation.viewmodel.comfyui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.domain.repositoryint.remote.ComfyRepository
import it.giovanni.hub.presentation.screen.detail.comfyui.ComfyUtils.buildHairColorRequestBody
import it.giovanni.hub.presentation.screen.detail.comfyui.ComfyUtils.uploadImageToComfyUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import java.net.URLEncoder
import java.net.UnknownHostException
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class HairColorViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: ComfyRepository
) : BaseViewModel(context) {

    fun generateImage(
        baseUrl: String,
        prompt: String,
        sourceImageUri: Uri,
        onResult: (Result<Unit>) -> Unit
    ) = viewModelScope.launch {
        try {
            // 1) Upload the selected image (gallery/camera) to ComfyUI
            val uploadedName = uploadImageToComfyUI(context, baseUrl.trim(), sourceImageUri)

            // 2) Build workflow body
            val (body, saveNodeId) = buildHairColorRequestBody(
                context = context,
                prompt = prompt,
                uploadedImageName = uploadedName
            )

            // 3) Start the run
            val startResponse: JsonObject = repository.startRun(body)

            Log.d("ComfyUI", "startRun = $startResponse")

            val promptId = startResponse.get("prompt_id")?.asString
                ?: startResponse.getAsJsonObject("prompt")?.get("id")?.asString
                ?: throw IllegalStateException("promptId not found in startRun response")

            Log.d("ComfyUI", "Queued promptId = $promptId, saveNodeId = $saveNodeId")

            // 4) Poll /history/{promptId} until SaveImage node produces output
            val finished = withTimeoutOrNull(120_000L) { // 2 minutes
                while (isActive) {
                    val historyRoot: JsonObject = repository.getRun(promptId = promptId)
                    Log.d("ComfyUI", "history($promptId) = $historyRoot")

                    // Pick the entry for this promptId
                    val entry = historyRoot.getAsJsonObject(promptId)
                    if (entry == null) {
                        // history not ready yet
                        delay(1_000)
                        continue
                    }

                    // Read status
                    val statusObj = entry.getAsJsonObject("status")
                    val statusStr = statusObj?.get("status_str")?.asString ?: ""
                    val completedFlag = statusObj?.get("completed")?.takeIf { it.isJsonPrimitive }?.asBoolean ?: false

                    if (statusStr == "error") {
                        // Try to extract the exception_message from messages
                        val messages = statusObj.getAsJsonArray("messages")
                        var errorMsg: String? = null
                        messages?.forEach { msgElem ->
                            val arr = msgElem.asJsonArray
                            if (arr.size() >= 2 && arr[0].asString == "execution_error") {
                                val payload = arr[1].asJsonObject
                                errorMsg = payload.get("exception_message")?.asString
                            }
                        }

                        val finalMsg = errorMsg ?: "ComfyUI reported an error in the workflow."
                        Log.e("ComfyUI", "Prompt $promptId failed: $finalMsg")

                        onResult(Result.failure(Exception(finalMsg)))
                        return@withTimeoutOrNull
                    }

                    val outputs = entry.getAsJsonObject("outputs")

                    // Read the SaveImage node
                    val saveNode = outputs?.getAsJsonObject(saveNodeId)
                    val imagesArray = saveNode?.getAsJsonArray("images")
                    val firstImage = imagesArray?.firstOrNull()?.asJsonObject

                    if (firstImage != null) {
                        val filename = firstImage["filename"].asString
                        val subfolder = firstImage["subfolder"].asString
                        val type = firstImage["type"].asString  // "output"

                        val encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString())
                        val encodedSubfolder = URLEncoder.encode(subfolder, StandardCharsets.UTF_8.toString())

                        val comfyUIBaseUrl = baseUrl.let {
                            if (it.endsWith("/")) it else "$it/"
                        }

                        imageUrl = "${comfyUIBaseUrl}view?filename=$encodedFilename&subfolder=$encodedSubfolder&type=$type"

                        // Use this if the URL is fixed and you don't need to read it from DataStoreRepository.
                        // imageUrl = "${Config.COMFY_BASE_URL}view?filename=$encodedFilename&subfolder=$encodedSubfolder&type=$type"

                        Log.d("ComfyUI", "Image URL = $imageUrl")

                        onResult(Result.success(Unit))
                        notifyImageReady(imageUrl)

                        // Exit the timeout block cleanly
                        return@withTimeoutOrNull
                    }

                    // If run is completed but no images were produced (SaveImage never produced images), stop:
                    if (completedFlag) {
                        Log.w("ComfyUI", "Prompt $promptId completed but produced no images")
                        return@withTimeoutOrNull
                    }

                    // If still running, but no image yet, keep polling.
                    delay(1_000)
                }
            } != null
            if (!finished) {
                Log.w("ComfyUI", "Timed-out waiting for prompt $promptId")
                onResult(Result.failure(Exception("Timed-out waiting for ComfyUI to finish.")))
            }
        } catch (e: UnknownHostException) {
            Log.e("ComfyUI", "Cannot resolve host", e)
            onResult(Result.failure(Exception("Unable to reach ComfyUI: " + e.message)))
        } catch (e: IOException) {
            Log.e("ComfyUI", "Network error", e)
            onResult(Result.failure(Exception("Network error while calling ComfyUI: " + e.message)))
        } catch (e: Exception) {
            Log.e("ComfyUI", "Unexpected error in generateImage", e)
            onResult(Result.failure(Exception("Unexpected error while generating image: " + e.message)))
        }
    }
}