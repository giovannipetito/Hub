package it.giovanni.hub.data.datasource

import io.reactivex.Single
import it.giovanni.hub.data.HubResult
import it.giovanni.hub.data.response.UsersResponse

interface UsersDataSource {

    suspend fun getUsers(page: Int): HubResult<UsersResponse>

    fun getRxUsers(page: Int): Single<UsersResponse>
}