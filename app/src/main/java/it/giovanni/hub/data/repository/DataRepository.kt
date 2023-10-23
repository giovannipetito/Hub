package it.giovanni.hub.data.repository

import io.reactivex.Single
import it.giovanni.hub.data.ApiService
import it.giovanni.hub.data.HubResult
import it.giovanni.hub.data.response.UsersResponse
import it.giovanni.hub.data.datasource.remote.DataDataSource
import it.giovanni.hub.data.response.CharactersResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val apiService: ApiService): DataDataSource {

    override suspend fun getUsers(page: Int): HubResult<UsersResponse> {
        return try {
            val response: UsersResponse = apiService.getUsers(page)
            HubResult.Success(response)
        } catch (e: Exception) {
            HubResult.Error(e.localizedMessage)
        }
    }

    override fun getRxUsers(page: Int): Single<UsersResponse> {
        val observable: Single<UsersResponse> = apiService.getRxUsers(page)
        return observable
    }

    override suspend fun getCharacters(page: Int): CharactersResponse {
        val response: CharactersResponse = apiService.getAllCharacters(page)
        return response
    }

    /*
    override suspend fun getCharacters(page: Int): HubResult<CharactersResponse> {
        return try {
            val response: CharactersResponse = apiService.getAllCharacters(page)
            HubResult.Success(response)
        } catch (e: Exception) {
            HubResult.Error(e.localizedMessage)
        }
    }
    */
}