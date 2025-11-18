package it.giovanni.hub.presentation.screen.detail.comfyui

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.InputStream

object ComfyUtils {

    // Builds the body required by ComfyUI for the txt2img workflow
    fun buildTextToImageRequestBody(context: Context, promptText: String): JsonObject {

        val workflowJson = getWorkflowJson(context, "txt2img_api.json")

        // overrides the prompt (node "6" in the graph)
        workflowJson["6"].asJsonObject
            .getAsJsonObject("inputs")
            .addProperty("text", promptText)

        return JsonObject().apply {
            add("prompt", workflowJson)
        }
    }

    // Load the local JSON
    private fun getWorkflowJson(context: Context, fileName: String): JsonObject {
        val assetManager: AssetManager = context.assets
        val inputStream: InputStream = assetManager.open(fileName)
        val jsonString: String = inputStream.bufferedReader().use { it.readText() }
        return JsonParser.parseString(jsonString).asJsonObject
    }
}