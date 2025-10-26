package it.giovanni.hub.domain.repositoryint.remote

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.data.response.CharactersResponse
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.domain.result.simple.HubResult

interface UsersRepository {

    suspend fun getCoroutinesUsers(page: Int): HubResult<List<User>> // todo: consider returning HubResult<UsersResponseDto>

    fun getRxJavaUsers(page: Int): Single<List<User>> // todo: consider returning HubResult<UsersResponseDto>

    suspend fun getCharacters(page: Int): CharactersResponse

    // suspend fun getCharacters(page: Int): HubResult<CharactersResponse>
}