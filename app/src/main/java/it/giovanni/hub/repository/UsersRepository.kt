package it.giovanni.hub.repository

import io.reactivex.Single
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

    override fun getRxUsers(page: Int): Single<UsersResponse> {
        val observable: Single<UsersResponse> = apiService.getRxUsers(page)
        return observable
    }
}