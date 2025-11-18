package it.giovanni.hub.data.api

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ComfyApiService {

    // Enqueue a workflow
    @POST("/prompt")
    suspend fun startPrompt(
        @Body body: JsonObject
    ): JsonObject

    // Read the history / outputs for a given prompt
    @GET("/history/{promptId}")
    suspend fun getHistory(
        @Path("promptId") promptId: String
    ): JsonObject
}