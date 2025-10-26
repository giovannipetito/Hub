package it.giovanni.hub.data.api

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.data.dto.UsersResponseDto
import it.giovanni.hub.data.response.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/users")
    suspend fun getCoroutinesUsers(
        @Query("page") page: Int
    ): UsersResponseDto

    @GET("/api/users")
    fun getRxJavaUsers(
        @Query("page") page: Int
    ): Single<UsersResponseDto>

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharactersResponse
}