package it.giovanni.hub.data.repositoryimpl.remote

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.data.api.ApiService
import it.giovanni.hub.data.dto.UsersResponseDto
import it.giovanni.hub.data.mapper.toDomain
import it.giovanni.hub.data.dto.CharactersResponse
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.domain.repositoryint.remote.UsersRepository
import it.giovanni.hub.domain.result.simple.HubResult
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UsersRepositoryImpl @Inject constructor(
    // private val apiService: ApiService // If I use just one instance of ApiService.
    @param:Named("baseUrl1") private val apiService1: ApiService,
    @param:Named("baseUrl2") private val apiService2: ApiService
): UsersRepository {

    override suspend fun getCoroutinesUsers(page: Int): HubResult<List<User>> {
        return try {
            val responseDto: UsersResponseDto = apiService1.getCoroutinesUsers(page)
            val mappedUsers = responseDto.data.map { it.toDomain() }
            // val mappedUiUsers = mappedUsers.map { it.toPresentation() }
            HubResult.Success(mappedUsers)
        } catch (e: Exception) {
            HubResult.Error(e.localizedMessage) // Or: e.stackTrace.toString()
        }
    }

    override fun getRxJavaUsers(page: Int): Single<List<User>> {
        val mappedUsers: Single<List<User>> = apiService1.getRxJavaUsers(page)
            .map {
                it.data.map {
                        userDto -> userDto.toDomain()
                }
            }
        return mappedUsers
    }

    override suspend fun getCharacters(page: Int): CharactersResponse {
        val response: CharactersResponse = apiService2.getCharacters(page)
        return response
    }
}