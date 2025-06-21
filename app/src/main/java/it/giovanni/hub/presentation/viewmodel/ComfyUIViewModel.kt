package it.giovanni.hub.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.core.IOException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ComfyUIViewModel @Inject constructor() : ViewModel() {

    var imageUrl by mutableStateOf<String?>(null)
        private set

    private val baseUrl = "http://192.168.1.10:8188"

    fun generateImage(context: Context, prompt: String) {
        val requestBody = buildRequestBody(context, prompt)

        viewModelScope.launch {
            try {
                val response = postRequest("$baseUrl/prompt", requestBody)
                val promptId = response.get("prompt_id").asString
                pollForResult(promptId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun pollForResult(promptId: String) {
        repeat(30) {
            delay(1000)
            try {
                val response = getRequest("$baseUrl/history/$promptId")
                val outputsElement = response["outputs"]

                if (outputsElement != null && outputsElement.isJsonArray) {
                    val outputs = outputsElement.asJsonArray
                    if (outputs.size() > 0) {
                        val imagesElement = outputs[0].asJsonObject["images"]
                        if (imagesElement != null && imagesElement.isJsonArray && imagesElement.asJsonArray.size() > 0) {
                            val image = imagesElement.asJsonArray[0].asJsonObject["filename"].asString
                            imageUrl = "$baseUrl/view?filename=$image"
                            return
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // logging utile per capire se il backend risponde in modo inatteso
            }
        }
    }

    private fun buildRequestBody(context: Context, prompt: String): JsonObject {
        val json = JsonObject()
        val workflow = getWorkflowJson(context)
        workflow.getAsJsonObject("8")
            .getAsJsonObject("inputs")
            .addProperty("text", prompt)
        json.add("prompt", workflow)
        return json
    }

    private fun getWorkflowJson(context: Context): JsonObject {
        val assetManager = context.assets
        val inputStream = assetManager.open("txt2img_api.json")
        val jsonStr = inputStream.bufferedReader().use { it.readText() }
        return JsonParser.parseString(jsonStr).asJsonObject
    }

    private suspend fun postRequest(url: String, body: JsonObject): JsonObject {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .post(RequestBody.create("application/json".toMediaType(), body.toString()))
                .build()

            client.newCall(request).execute().use {
                val bodyStr = it.body?.string() ?: throw IOException("Empty response")
                JsonParser.parseString(bodyStr).asJsonObject
            }
        }
    }

    private suspend fun getRequest(url: String): JsonObject {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).get().build()

            client.newCall(request).execute().use {
                val bodyStr = it.body?.string() ?: throw IOException("Empty response")
                JsonParser.parseString(bodyStr).asJsonObject
            }
        }
    }
}