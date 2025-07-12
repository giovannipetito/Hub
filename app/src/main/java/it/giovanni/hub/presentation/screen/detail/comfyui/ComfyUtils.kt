package it.giovanni.hub.presentation.screen.detail.comfyui

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.giovanni.hub.presentation.viewmodel.ComfyUIViewModel.Companion.TXT2IMG_WORKFLOW_ID
import java.io.InputStream

object ComfyUtils {

    /** Builds the body required by ComfyICU for the txt2img workflow */
    fun buildTextToImageRequestBody(context: Context, promptText: String): JsonObject {
        val workflowJson = getWorkflowJson(context, "txt2img_api.json")
        // overrides the prompt (node "8" in the graph)
        workflowJson["8"].asJsonObject["inputs"].asJsonObject.addProperty("text", promptText)
        return JsonObject().apply {
            addProperty("workflow_id", TXT2IMG_WORKFLOW_ID)
            add("prompt", workflowJson)
        }
    }

    /** Load the local JSON */
    private fun getWorkflowJson(context: Context, fileName: String): JsonObject {
        val assetManager: AssetManager = context.assets
        val inputStream: InputStream = assetManager.open(fileName)
        val jsonString: String = inputStream.bufferedReader().use { it.readText() }
        return JsonParser.parseString(jsonString).asJsonObject
    }
}