package it.giovanni.hub.data.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

// We build the paths /prompt and /history/{promptId} in the repository.
interface ComfyApiService {

    // Enqueue a workflow
    @POST
    suspend fun startPrompt(
        @Url url: String,
        @Body body: JsonObject
    ): JsonObject

    // Read the history / outputs for a given prompt
    @GET
    suspend fun getHistory(
        @Url url: String
    ): JsonObject

    @Multipart
    @POST
    suspend fun uploadImage(
        @Url url: String,
        @Part image: MultipartBody.Part
    ): JsonObject
}

/*
Use this if the URL is fixed and you don't need to read it from DataStoreRepository.
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
*/