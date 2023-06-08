package it.giovanni.hub.datasource

import io.reactivex.Single
import it.giovanni.hub.HubResult
import it.giovanni.hub.UsersResponse

interface UsersDataSource {

    suspend fun getUsers(page: Int): HubResult<UsersResponse>

    fun getRxUsers(page: Int): Single<UsersResponse>
}