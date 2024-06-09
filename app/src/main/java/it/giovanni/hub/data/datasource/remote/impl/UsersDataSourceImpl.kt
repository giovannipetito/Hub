package it.giovanni.hub.data.datasource.remote.impl

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.data.ApiService
import it.giovanni.hub.domain.result.simple.HubResult
import it.giovanni.hub.data.response.UsersResponse
import it.giovanni.hub.data.datasource.remote.UsersDataSource
import it.giovanni.hub.data.response.CharactersResponse
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UsersDataSourceImpl @Inject constructor(
    // private val apiService: ApiService // If I use just one instance of ApiService.
    @Named("baseUrl1") private val apiService1: ApiService,
    @Named("baseUrl2") private val apiService2: ApiService
): UsersDataSource {

    override suspend fun getCoroutinesUsers(page: Int): HubResult<UsersResponse> {
        return try {
            val response: UsersResponse = apiService1.getCoroutinesUsers(page)
            HubResult.Success(response)
        } catch (e: Exception) {
            HubResult.Error(e.localizedMessage) // Oppure: e.stackTrace.toString()
        }
    }

    override fun getRxUsers(page: Int): Single<UsersResponse> {
        val observable: Single<UsersResponse> = apiService1.getRxUsers(page)
        return observable
    }

    override suspend fun getCharacters(page: Int): CharactersResponse {
        val response: CharactersResponse = apiService2.getCharacters(page)
        return response
    }
}