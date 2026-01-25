package it.giovanni.hub.domain.repository.remote

import com.google.gson.JsonObject

interface ComfyRepository {

    suspend fun startRun(
        body: JsonObject
    ): JsonObject

    suspend fun getRun(
        promptId: String
    ): JsonObject

    suspend fun uploadImage(
        bytes: ByteArray,
        mimeType: String,
        fileName: String
    ): String
}