package it.giovanni.hub.presentation.screen.detail.comfyui

import android.content.Context
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.giovanni.hub.presentation.viewmodel.ComfyUIViewModel.Companion.TXT2IMG_WORKFLOW_ID

object ComfyUtils {

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
}