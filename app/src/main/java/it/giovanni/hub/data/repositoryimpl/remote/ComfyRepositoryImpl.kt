package it.giovanni.hub.data.repositoryimpl.remote

import com.google.gson.JsonObject
import it.giovanni.hub.data.api.ComfyApiService
import it.giovanni.hub.data.repositoryimpl.local.DataStoreRepository
import it.giovanni.hub.domain.repositoryint.remote.ComfyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ComfyRepositoryImpl @Inject constructor(
    @param:Named("comfyBaseUrl") private val comfyApiService: ComfyApiService,
    private val dataStore: DataStoreRepository
): ComfyRepository {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    private suspend fun getBaseUrl(): String {
        val savedUrl: String? = dataStore.getComfyUIBaseUrl().firstOrNull()
        return savedUrl?.ensureTrailingSlash()!!
    }

    override suspend fun startRun(
        body: JsonObject
    ): JsonObject = withContext(dispatcher) {
        val baseUrl = getBaseUrl()
        val url = "${baseUrl}prompt"
        comfyApiService.startPrompt(url, body)
    }

    override suspend fun getRun(
        promptId: String
    ): JsonObject = withContext(dispatcher) {
        val baseUrl = getBaseUrl()
        val url = "${baseUrl}history/$promptId"
        comfyApiService.getHistory(url)
    }

    /*
    Use this if the URL is fixed and you don't need to read it from DataStoreRepository.
    suspend fun startRun(
        body: JsonObject
    ): JsonObject = withContext(dispatcher) {
        comfyApiService.startPrompt(body)
    }

    suspend fun getRun(
        promptId: String
    ): JsonObject = withContext(dispatcher) {
        comfyApiService.getHistory(promptId)
    }
    */

    private fun String.ensureTrailingSlash(): String =
        if (endsWith("/")) this else "$this/"
}