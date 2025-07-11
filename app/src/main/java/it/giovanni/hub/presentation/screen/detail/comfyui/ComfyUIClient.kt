package it.giovanni.hub.presentation.screen.detail.comfyui

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.giovanni.hub.presentation.viewmodel.ComfyUIViewModel.Companion.API_KEY
import it.giovanni.hub.presentation.viewmodel.ComfyUIViewModel.Companion.TXT2IMG_WORKFLOW_ID
import it.giovanni.hub.utils.Config.COMFY_ICU_BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object ComfyUIClient {

    /** Costruisce il body richiesto da ComfyICU */
    fun buildRequestBody(context: Context, promptText: String): JsonObject {
        // Carica il tuo JSON locale
        val workflowJson = getWorkflowJson(context, "txt2img_api.json")
        // sovrascrive il prompt (nodo "8" nel tuo grafo)
        workflowJson["8"].asJsonObject["inputs"].asJsonObject.addProperty("text", promptText)
        // Costruisci il body da inviare a Comfy.ICU
        return JsonObject().apply {
            addProperty("workflow_id", TXT2IMG_WORKFLOW_ID)
            add("prompt", workflowJson) // l’intero grafo
            // se devi allegare modelli / LoRA custom:
            // add("files", filesJson)
        }
    }

    private fun getWorkflowJson(context: Context, fileName: String): JsonObject {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return JsonParser.parseString(jsonString).asJsonObject
    }

    /** --- networking helpers con header Bearer ---------------------------------- */
    suspend fun postRequest(url: String, body: JsonObject): JsonObject = withContext(Dispatchers.IO) {
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

    suspend fun getRequest(url: String): JsonObject = withContext(Dispatchers.IO) {
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

    suspend fun fetchRuns(limit: Int): JsonArray = withContext(Dispatchers.IO) {
        val url = "$COMFY_ICU_BASE_URL/workflows/$TXT2IMG_WORKFLOW_ID/runs?limit=$limit"

        val jsonString = OkHttpClient().newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("accept", "application/json")
                .get()
                .build()
        ).execute().use { it.body.string() }

        JsonParser.parseString(jsonString).asJsonArray
    }
}