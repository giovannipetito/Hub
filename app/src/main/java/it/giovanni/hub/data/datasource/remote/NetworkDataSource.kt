package it.giovanni.hub.data.datasource.remote

import it.giovanni.hub.data.request.NetworkRequest
import it.giovanni.hub.data.response.NetworkResponse
import it.giovanni.hub.domain.result.simple.HubResult

interface NetworkDataSource {
    suspend fun sendMessage(request: NetworkRequest): HubResult<NetworkResponse>
}