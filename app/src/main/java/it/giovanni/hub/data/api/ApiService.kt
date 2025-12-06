package it.giovanni.hub.data.api

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.data.dto.UsersResponseDto
import it.giovanni.hub.data.dto.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Clean Architecture:
 * ApiService è nel data layer. Espone le operazioni HTTP.
 *
 * Sono esposti due metodi per dimostrare:
 * - Lo stesso endpoint può essere consumato sia con coroutines che con Rx,
 *   lasciando al domain/presentation la scelta del paradigma.
 * - Retrocompatibilità / migrazioni graduali da Rx a Coroutines.
 *
 * Coroutines:
 * suspend usa l’adapter nativo di Retrofit per le coroutines; la chiamata
 * è sospesa ed è restituita su success/failure (via eccezioni).
 *
 * RxJava:
 * versione parallela che restituisce Single<UsersResponseDto>, adatta a catene Rx.
 *
 * Error handling:
 * - Con coroutines si catturano eccezioni (try/catch).
 * - Con RxJava si propagano errori sul canale onError.
 *
 * Threading:
 * - Con coroutines lo scheduler si decide a livello superiore: withContext(Dispatchers.IO) nel
 *   repository o Retrofit + OkHttp.
 * - Con RxJava lo scheduler si decide con subscribeOn/observeOn.
 */
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