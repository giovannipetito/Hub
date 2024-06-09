package it.giovanni.hub.data

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.data.response.CharactersResponse
import it.giovanni.hub.data.response.UsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/users")
    suspend fun getCoroutinesUsers(
        @Query("page") page: Int,
    ): UsersResponse

    @GET("/api/users")
    fun getRxUsers(
        @Query("page") page: Int,
    ): Single<UsersResponse>

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharactersResponse
}