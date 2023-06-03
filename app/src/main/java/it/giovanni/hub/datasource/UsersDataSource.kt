package it.giovanni.hub.datasource

import it.giovanni.hub.HubResult
import it.giovanni.hub.UsersResponse

interface UsersDataSource {

    suspend fun getUsers(page: Int): HubResult<UsersResponse>
}