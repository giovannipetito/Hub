package it.giovanni.hub.domain.repositoryint.remote

import com.google.gson.JsonObject

interface ComfyRepository {

    suspend fun startRun(body: JsonObject): JsonObject

    suspend fun getRun(runId: String): JsonObject
}