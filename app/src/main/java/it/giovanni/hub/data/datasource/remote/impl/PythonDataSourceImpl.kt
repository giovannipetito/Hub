package it.giovanni.hub.data.datasource.remote.impl

import it.giovanni.hub.data.ApiService
import it.giovanni.hub.data.datasource.remote.PythonDataSource
import it.giovanni.hub.data.request.PythonMessageRequest
import it.giovanni.hub.data.response.PythonMessageResponse
import it.giovanni.hub.domain.result.simple.HubResult
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PythonDataSourceImpl @Inject constructor(
    @Named("baseUrl3") private val apiService3: ApiService
): PythonDataSource {

    override suspend fun sendMessage(request: PythonMessageRequest): HubResult<PythonMessageResponse> {
        return try {
            val response: PythonMessageResponse = apiService3.sendMessage(request)
            HubResult.Success(response)
        } catch (e: Exception) {
            HubResult.Error(e.localizedMessage) // Oppure: e.stackTrace.toString()
        }
    }
}