package it.giovanni.hub.presentation.viewmodel.comfyui

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import it.giovanni.hub.domain.repositoryint.remote.ComfyRepository
import it.giovanni.hub.presentation.screen.detail.comfyui.ComfyUtils.buildHairColorRequestBody
import it.giovanni.hub.presentation.screen.detail.comfyui.ComfyUtils.buildTextToImageRequestBody
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
class TextToImageViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: ComfyRepository
) : BaseViewModel(context) {

    fun generateImage(
        comfyUrl: String,
        prompt: String,
        onResult: (Result<Unit>) -> Unit
    ) = viewModelScope.launch {
        try {
            val (body, saveNodeId) = buildTextToImageRequestBody(
                context = context,
                prompt = prompt
            )

            val startResponse: JsonObject = repository.startRun(body)
            val promptId = startResponse["prompt_id"].asString
            Log.d("ComfyUI", "Queued promptId=$promptId")

            withTimeoutOrNull(120_000L) { // 2 minutes
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
                    val completedFlag = statusObj?.get("completed")?.takeIf { it.isJsonPrimitive }?.asBoolean ?: false

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

                        val baseUrl = comfyUrl.let {
                            if (it.endsWith("/")) it else "$it/"
                        }

                        imageUrl = "${baseUrl}view?filename=$encodedFilename&subfolder=$encodedSubfolder&type=$type"

                        // Use this if the URL is fixed and you don't need to read it from DataStoreRepository.
                        // imageUrl = "${Config.COMFY_BASE_URL}view?filename=$encodedFilename&subfolder=$encodedSubfolder&type=$type"

                        Log.d("ComfyUI", "Image URL = $imageUrl")

                        onResult(Result.success(Unit))
                        notifyImageReady(imageUrl)

                        // Exit the timeout block cleanly
                        return@withTimeoutOrNull
                    }

                    // Run is marked completed but no images were produced:
                    if (completedFlag) {
                        Log.w("ComfyUI", "Prompt $promptId completed but produced no images")
                        return@withTimeoutOrNull
                    }

                    // If no image yet, just keep polling (even if completedFlag is true, in practice
                    // you'll have outputs together with completed=true)
                    delay(1_000)
                }
            } ?: Log.w("ComfyUI", "Timed-out waiting for prompt $promptId")
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