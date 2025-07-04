package it.giovanni.hub.presentation.viewmodel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.giovanni.hub.BuildConfig
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeoutOrNull
import java.io.OutputStream

@HiltViewModel
class ComfyUIViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    companion object {
        private const val TXT2IMG_WORKFLOW_ID = "QfPQGkm_3sklkqeotTb_L"
        private const val API_KEY = BuildConfig.COMFY_ICU_KEY
        private const val COMFY_ICU_BASE_URL = "https://comfy.icu/api/v1"
    }

    var imageUrl by mutableStateOf<String?>(null)
        private set

    private val _saveResult = MutableSharedFlow<Boolean>()
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    /** POST /workflows/{id}/runs */
    fun generateImage(promptText: String) = viewModelScope.launch {
        val body = buildRequestBody(promptText)
        val run = postRequest("$COMFY_ICU_BASE_URL/workflows/$TXT2IMG_WORKFLOW_ID/runs", body)
        val runId = run["id"].asString
        pollForResult(runId)
    }

    /** Costruisce il body richiesto da ComfyICU */
    private fun buildRequestBody(text: String): JsonObject {
        // Carica il tuo JSON locale
        val workflow = getWorkflowJson(context)
        // sovrascrive il prompt (nodo "8" nel tuo grafo)
        workflow["8"].asJsonObject["inputs"].asJsonObject.addProperty("text", text)
        // Costruisci il body da inviare a Comfy.ICU
        return JsonObject().apply {
            addProperty("workflow_id", TXT2IMG_WORKFLOW_ID)
            add("prompt", workflow) // l’intero grafo
            // se devi allegare modelli / LoRA custom:
            // add("files", filesJson)
        }
    }

    /*
    VECCHIO METODO

    private suspend fun pollForResult(runId: String) {
        repeat(30) {
            delay(1000)
            val resp = getRequest("$COMFY_ICU_BASE_URL/workflows/$TXT2IMG_WORKFLOW_ID/runs/$runId")
            if (resp["status"].asString == "COMPLETED") {
                val outputs = resp["output"].asJsonArray
                if (outputs.size() > 0) {
                    imageUrl = outputs[0].asJsonObject["url"].asString
                }
                return
            }
        }
    }
    */

    /** GET /workflows/{id}/runs/{runId} (ripete finché COMPLETED) */
    private suspend fun pollForResult(runId: String) {
        withTimeoutOrNull(120_000) { // 2 min budget
            while (isActive) {
                val resp = getRequest("$COMFY_ICU_BASE_URL/workflows/$TXT2IMG_WORKFLOW_ID/runs/$runId")
                if (resp["status"].asString == "COMPLETED") {
                    val outputs = resp["output"].asJsonArray
                    outputs.firstOrNull()?.let { json: JsonElement ->
                        imageUrl = json.asJsonObject["url"].asString
                    }
                    cancel() // leave the loop
                }
                delay(1_000)
            }
        } ?: Log.w("ComfyUI", "Timed-out waiting for workflow $runId")
    }

    private fun getWorkflowJson(context: Context): JsonObject {
        val assetManager = context.assets
        val inputStream = assetManager.open("txt2img_api.json")
        val jsonStr = inputStream.bufferedReader().use { it.readText() }
        return JsonParser.parseString(jsonStr).asJsonObject
    }

    /** --- networking helpers con header Bearer ---------------------------------- */
    private suspend fun postRequest(url: String, body: JsonObject) = withContext(Dispatchers.IO) {
        OkHttpClient().newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .post(body.toString().toRequestBody("application/json".toMediaType()))
                .build()
        ).execute().use {
            JsonParser.parseString(it.body.string()).asJsonObject
        }
    }

    private suspend fun getRequest(url: String) = withContext(Dispatchers.IO) {
        OkHttpClient().newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("accept", "application/json")
                .get()
                .build()
        ).execute().use {
            JsonParser.parseString(it.body.string()).asJsonObject
        }
    }

    fun saveImageToGallery() {
        val url = imageUrl ?: return
        viewModelScope.launch {
            _saveResult.emit( saveViaMediaStore(context, url) )
        }
    }

    private suspend fun saveViaMediaStore(context: Context, url: String): Boolean =
        withContext(Dispatchers.IO) {
            runCatching {
                val filename = "comfy_${System.currentTimeMillis()}.jpg"

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/ComfyUI")
                }

                val resolver = context.contentResolver
                val uri = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                ) ?: return@runCatching false

                resolver.openOutputStream(uri).use { outputStream: OutputStream? ->
                    // download & decode with Coil (already on class-path)
                    val drawable = ImageLoader(context).execute(
                        ImageRequest.Builder(context)
                            .data(url)
                            .allowHardware(false)
                            .build()
                    ).drawable ?: return@runCatching false

                    if (outputStream != null)
                            (drawable as BitmapDrawable).bitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)
                }
                true
            }.getOrDefault(false)
        }
}