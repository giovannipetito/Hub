package it.giovanni.hub.data

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ComfyApiService {

    @POST("/api/v1/workflows/{id}/runs")
    suspend fun startRun(
        @Path("id") workflowId: String,
        @Body body: JsonObject
    ): JsonObject

    @GET("/api/v1/workflows/{id}/runs/{runId}")
    suspend fun getRun(
        @Path("id") workflowId: String,
        @Path("runId") runId: String
    ): JsonObject

    @GET("/api/v1/workflows/{id}/runs") // /api/v1/workflows/{id}/runs?limit={limit}
    suspend fun fetchRuns(
        @Path("id") workflowId: String,
        @Query("limit") limit: Int = 50
    ): JsonArray
}