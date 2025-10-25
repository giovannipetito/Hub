package it.giovanni.hub.domain.repositoryint.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject

interface ComfyRepository {

    suspend fun startRun(workflowId: String, body: JsonObject): JsonObject

    suspend fun getRun(workflowId: String, runId: String): JsonObject

    suspend fun fetchRuns(workflowId: String, limit: Int): JsonArray
}