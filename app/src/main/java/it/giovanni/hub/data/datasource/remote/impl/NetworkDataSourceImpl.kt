package it.giovanni.hub.data.datasource.remote.impl

import it.giovanni.hub.data.ApiService
import it.giovanni.hub.data.datasource.remote.NetworkDataSource
import it.giovanni.hub.data.request.NetworkRequest
import it.giovanni.hub.data.response.NetworkResponse
import it.giovanni.hub.domain.result.simple.HubResult
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class NetworkDataSourceImpl @Inject constructor(
    @Named("baseUrl3") private val apiService3: ApiService
): NetworkDataSource {

    override suspend fun sendMessage(request: NetworkRequest): HubResult<NetworkResponse> {
        return try {
            val response: NetworkResponse = apiService3.sendMessage(request)
            HubResult.Success(response)
        } catch (e: Exception) {
            HubResult.Error(e.localizedMessage) // Oppure: e.stackTrace.toString()
        }
    }
}