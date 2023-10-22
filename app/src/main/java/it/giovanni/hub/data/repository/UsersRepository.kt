package it.giovanni.hub.data.repository

import io.reactivex.Single
import it.giovanni.hub.data.ApiService
import it.giovanni.hub.data.HubResult
import it.giovanni.hub.data.response.UsersResponse
import it.giovanni.hub.data.datasource.UsersDataSource
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

    override fun getRxUsers(page: Int): Single<UsersResponse> {
        val observable: Single<UsersResponse> = apiService.getRxUsers(page)
        return observable
    }
}