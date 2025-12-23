package it.giovanni.hub.domain.workflow

import android.content.Context
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.InputStream

object ComfyUtils {

    /**
     * Builds the body required by ComfyUI for the txt2img workflow
     *
     * Scan all nodes once and detect them dynamically (whatever id ComfyUI assigned):
     * - Positive CLIPTextEncode (prompt)
     * - SaveImage node
     */
    fun buildTextToImageRequestBody(
        context: Context,
        prompt: String
    ): Pair<JsonObject, String> {

        val workflowJson = getWorkflowJson(context, "txt2img_api.json")

        var clipTextNodeId: String? = null
        var saveNodeId: String? = null

        for ((key, value) in workflowJson.entrySet()) {
            val nodeObj = value.asJsonObject
            val classType = nodeObj.get("class_type")?.asString

            when (classType) {
                "CLIPTextEncode" -> {
                    val inputsObj = nodeObj.getAsJsonObject("inputs")
                    val defaultText = inputsObj?.get("text")?.asString ?: ""

                    // In your workflow (txt2img_api.json):
                    // - Node 1 is negative: "(low quality, worst quality:1.4)..."
                    // - Node 6 is positive: "A cute red fox..."
                    // So we pick the CLIPTextEncode that is NOT the typical "low quality / worst quality" negative prompt.
                    if (!defaultText.contains("low quality", ignoreCase = true) &&
                        !defaultText.contains("worst quality", ignoreCase = true)
                    ) {
                        clipTextNodeId = key
                    }
                }

                "SaveImage" -> {
                    if (saveNodeId == null) {
                        saveNodeId = key
                    }
                }
            }
        }

        if (clipTextNodeId == null) {
            throw IllegalStateException("No positive CLIPTextEncode node found in txt2img_api.json")
        }
        if (saveNodeId == null) {
            throw IllegalStateException("No SaveImage node found in txt2img_api.json")
        }

        // Dynamic CLIPTextEncode node: override the positive prompt (dynamic node instead of hard-coded "6")
        workflowJson.getAsJsonObject(clipTextNodeId)
            ?.getAsJsonObject("inputs")
            ?.addProperty("text", prompt)

        // Wrap for /prompt call and return (body, saveNodeId)
        return JsonObject().apply {
            add("prompt", workflowJson)
        } to saveNodeId
    }

    /**
     * Builds the body required by ComfyUI for the img2img workflow
     *
     * Scan all nodes once and detect them dynamically (whatever id ComfyUI assigned):
     * - Positive CLIPTextEncode node (heuristic: not the "low quality" negative one)
     * - LoadImage node
     * - SaveImage node
     */
    fun buildHairColorRequestBody(
        context: Context,
        prompt: String,
        uploadedImageName: String?
    ): Pair<JsonObject, String> {

        val workflowJson = getWorkflowJson(context, "img2img_hair_color_api.json")

        var clipTextNodeId: String? = null
        var loadImageNodeId: String? = null
        var saveNodeId: String? = null

        for ((key, value) in workflowJson.entrySet()) {
            val nodeObj = value.asJsonObject
            val classType = nodeObj.get("class_type")?.asString

            when (classType) {
                "CLIPTextEncode" -> {
                    val inputsObj = nodeObj.getAsJsonObject("inputs")
                    val defaultText = inputsObj?.get("text")?.asString ?: ""

                    // In your workflow (img2img_hair_color_api.json):
                    // - Node 1  is negative: "(low quality, worst quality:1.4)..."
                    // - Node 23 is positive: "multicolor hair"
                    // So we pick the CLIPTextEncode that is NOT the typical "low quality / worst quality" negative prompt.
                    if (!defaultText.contains("low quality", ignoreCase = true) &&
                        !defaultText.contains("worst quality", ignoreCase = true)
                    ) {
                        clipTextNodeId = key
                    }
                }

                "LoadImage" -> {
                    if (loadImageNodeId == null) {
                        loadImageNodeId = key
                    }
                }

                "SaveImage" -> {
                    if (saveNodeId == null) {
                        saveNodeId = key
                    }
                }
            }
        }

        if (clipTextNodeId == null) {
            throw IllegalStateException("No positive CLIPTextEncode node found in img2img_hair_color_api.json")
        }
        if (loadImageNodeId == null) {
            throw IllegalStateException("No LoadImage node found in img2img_hair_color_api.json")
        }
        if (saveNodeId == null) {
            throw IllegalStateException("No SaveImage node found in img2img_hair_color_api.json")
        }

        // Dynamic CLIPTextEncode node: override the positive prompt ("red hair" -> "${selected.name} hair")
        workflowJson.getAsJsonObject(clipTextNodeId)
            ?.getAsJsonObject("inputs")
            ?.addProperty("text", prompt)

        // Dynamic LoadImage node: override the image file name
        if (!uploadedImageName.isNullOrBlank()) {
            workflowJson.getAsJsonObject(loadImageNodeId)
                ?.getAsJsonObject("inputs")
                ?.addProperty("image", uploadedImageName)
        }

        val body = JsonObject().apply {
            add("prompt", workflowJson)
        }

        return body to saveNodeId
    }

    // Load the local JSON
    private fun getWorkflowJson(context: Context, fileName: String): JsonObject {
        val inputStream: InputStream = context.assets.open(fileName)
        val jsonString: String = inputStream.bufferedReader().use { it.readText() }
        return JsonParser.parseString(jsonString).asJsonObject
    }
}