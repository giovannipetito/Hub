package it.giovanni.hub.data.datasource.remote

import it.giovanni.hub.data.request.PythonMessageRequest
import it.giovanni.hub.data.response.PythonMessageResponse
import it.giovanni.hub.domain.result.simple.HubResult

interface PythonDataSource {
    suspend fun sendMessage(request: PythonMessageRequest): HubResult<PythonMessageResponse>
}