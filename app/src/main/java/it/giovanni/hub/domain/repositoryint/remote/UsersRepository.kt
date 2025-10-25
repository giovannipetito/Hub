package it.giovanni.hub.domain.repositoryint.remote

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.data.response.CharactersResponse
import it.giovanni.hub.data.response.UsersResponse
import it.giovanni.hub.domain.result.simple.HubResult

interface UsersRepository {

    suspend fun getCoroutinesUsers(page: Int): HubResult<UsersResponse>

    fun getRxJavaUsers(page: Int): Single<UsersResponse>

    suspend fun getCharacters(page: Int): CharactersResponse

    // suspend fun getCharacters(page: Int): HubResult<CharactersResponse>
}