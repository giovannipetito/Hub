package it.giovanni.hub.repository

import it.giovanni.hub.ApiService
import it.giovanni.hub.HubResult
import it.giovanni.hub.UsersResponse
import it.giovanni.hub.datasource.UsersDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(private val apiService: ApiService): UsersDataSource {

    override suspend fun getUsers(page: Int): HubResult<UsersResponse> {
        return try {
            val response: UsersResponse = apiService.getUsers(page)
            HubResult.Success(response)
        } catch (e: Exception) {
            HubResult.Error(e.localizedMessage)
        }
    }
}