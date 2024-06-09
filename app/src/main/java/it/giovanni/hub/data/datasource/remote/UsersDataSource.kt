package it.giovanni.hub.data.datasource.remote

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.domain.result.simple.HubResult
import it.giovanni.hub.data.response.CharactersResponse
import it.giovanni.hub.data.response.UsersResponse

interface UsersDataSource {

    suspend fun getCoroutinesUsers(page: Int): HubResult<UsersResponse>

    fun getRxUsers(page: Int): Single<UsersResponse>

    suspend fun getCharacters(page: Int): CharactersResponse

    // suspend fun getCharacters(page: Int): HubResult<CharactersResponse>
}