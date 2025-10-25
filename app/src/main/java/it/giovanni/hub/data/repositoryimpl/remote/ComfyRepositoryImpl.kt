package it.giovanni.hub.data.repositoryimpl.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import it.giovanni.hub.data.ComfyApiService
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
        workflowId: String,
        body: JsonObject
    ): JsonObject = withContext(dispatcher) {
        comfyApiService.startRun(workflowId, body)
    }

    override suspend fun getRun(
        workflowId: String,
        runId: String
    ): JsonObject = withContext(dispatcher) {
        comfyApiService.getRun(workflowId, runId)
    }

    override suspend fun fetchRuns(
        workflowId: String,
        limit: Int
    ): JsonArray = withContext(dispatcher) {
        comfyApiService.fetchRuns(workflowId, limit)
    }
}