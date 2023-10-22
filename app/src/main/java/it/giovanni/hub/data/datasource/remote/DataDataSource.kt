package it.giovanni.hub.data.datasource.remote

import io.reactivex.Single
import it.giovanni.hub.data.HubResult
import it.giovanni.hub.data.response.CharactersResponse
import it.giovanni.hub.data.response.UsersResponse

interface DataDataSource {

    suspend fun getUsers(page: Int): HubResult<UsersResponse>

    fun getRxUsers(page: Int): Single<UsersResponse>

    suspend fun getCharacters(page: Int): HubResult<CharactersResponse>
}