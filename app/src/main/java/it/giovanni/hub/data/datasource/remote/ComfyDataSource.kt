package it.giovanni.hub.data.datasource.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject

interface ComfyDataSource {

    suspend fun startRun(workflowId: String, body: JsonObject): JsonObject

    suspend fun getRun(workflowId: String, runId: String): JsonObject

    suspend fun fetchRuns(workflowId: String, limit: Int): JsonArray
}