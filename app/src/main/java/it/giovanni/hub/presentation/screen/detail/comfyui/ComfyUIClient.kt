package it.giovanni.hub.presentation.screen.detail.comfyui

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import it.giovanni.hub.BuildConfig
import it.giovanni.hub.presentation.viewmodel.ComfyUIViewModel.Companion.TXT2IMG_WORKFLOW_ID
import it.giovanni.hub.utils.Config.COMFY_ICU_BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object ComfyUIClient {

    /** --- networking helpers con header Bearer ---------------------------------- */
    suspend fun postRequest(url: String, body: JsonObject): JsonObject = withContext(Dispatchers.IO) {
        OkHttpClient().newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer ${BuildConfig.COMFY_ICU_API_KEY}")
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
                .addHeader("Authorization", "Bearer ${BuildConfig.COMFY_ICU_API_KEY}")
                .addHeader("accept", "application/json")
                .get()
                .build()
        ).execute().use {
            JsonParser.parseString(it.body.string()).asJsonObject
        }
    }

    suspend fun fetchRuns(limit: Int): JsonArray = withContext(Dispatchers.IO) {
        val url = "$COMFY_ICU_BASE_URL/api/v1/workflows/$TXT2IMG_WORKFLOW_ID/runs?limit=$limit"

        val jsonString = OkHttpClient().newCall(
            Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer ${BuildConfig.COMFY_ICU_API_KEY}")
                .addHeader("accept", "application/json")
                .get()
                .build()
        ).execute().use { it.body.string() }

        JsonParser.parseString(jsonString).asJsonArray
    }
}