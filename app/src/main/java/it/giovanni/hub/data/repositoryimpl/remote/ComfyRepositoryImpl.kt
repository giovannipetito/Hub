package it.giovanni.hub.data.repositoryimpl.remote

import com.google.gson.JsonObject
import it.giovanni.hub.data.api.ComfyApiService
import it.giovanni.hub.data.repositoryimpl.local.DataStoreRepository
import it.giovanni.hub.domain.repositoryint.remote.ComfyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    override suspend fun uploadImage(
        bytes: ByteArray,
        mimeType: String,
        fileName: String
    ): String = withContext(dispatcher) {
        val baseUrl = getBaseUrl()
        val url = "${baseUrl}upload/image"

        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", fileName, requestBody)

        val responseJson = comfyApiService.uploadImage(url, imagePart)

        val imagesArray = responseJson.getAsJsonArray("images")
        if (imagesArray != null && imagesArray.size() > 0) {
            imagesArray[0].asJsonObject.get("name").asString
        } else {
            responseJson.get("name").asString
        }
    }

    private fun String.ensureTrailingSlash(): String =
        if (endsWith("/")) this else "$this/"
}