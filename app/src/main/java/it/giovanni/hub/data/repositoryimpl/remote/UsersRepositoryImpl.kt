package it.giovanni.hub.data.repositoryimpl.remote

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.data.api.ApiService
import it.giovanni.hub.data.response.CharactersResponse
import it.giovanni.hub.data.response.UsersResponse
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

    override suspend fun getCoroutinesUsers(page: Int): HubResult<UsersResponse> {
        return try {
            val response: UsersResponse = apiService1.getCoroutinesUsers(page)
            HubResult.Success(response)
        } catch (e: Exception) {
            HubResult.Error(e.localizedMessage) // Or: e.stackTrace.toString()
        }
    }

    override fun getRxJavaUsers(page: Int): Single<UsersResponse> {
        val observable: Single<UsersResponse> = apiService1.getRxJavaUsers(page)
        return observable
    }

    override suspend fun getCharacters(page: Int): CharactersResponse {
        val response: CharactersResponse = apiService2.getCharacters(page)
        return response
    }
}