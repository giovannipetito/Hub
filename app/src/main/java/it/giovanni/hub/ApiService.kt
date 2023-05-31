package it.giovanni.hub

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/api/users")
    suspend fun getUsers(
        @Query("page") page: Int,
    ): UsersResponse
}