package it.giovanni.hub.data.repositoryimpl.remote

import com.google.gson.JsonObject
import it.giovanni.hub.data.api.ComfyApiService
import it.giovanni.hub.domain.repositoryint.remote.ComfyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ComfyRepositoryImpl @Inject constructor(
    @param:Named("comfyBaseUrl") private val comfyApiService: ComfyApiService
): ComfyRepository {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun startRun(
        body: JsonObject
    ): JsonObject = withContext(dispatcher) {
        comfyApiService.startPrompt(body)
    }

    override suspend fun getRun(
        promptId: String
    ): JsonObject = withContext(dispatcher) {
        comfyApiService.getHistory(promptId)
    }
}