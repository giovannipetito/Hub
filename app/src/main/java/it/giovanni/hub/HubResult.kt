package it.giovanni.hub

sealed class HubResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : HubResult<T>()
    data class Error(val message: String?, val statusCode: Int? = null) : HubResult<Nothing>()
}