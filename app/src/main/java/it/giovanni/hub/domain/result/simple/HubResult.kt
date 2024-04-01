package it.giovanni.hub.domain.result.simple

// 1° version
sealed class HubResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : HubResult<T>()
    data class Error(val message: String?, val statusCode: Int? = null) : HubResult<Nothing>()
}

/*
2° version
sealed class HubResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?): HubResult<T>(data)
    class Error<T>(message: String?, data: T? = null): HubResult<T>(data, message)
}
*/