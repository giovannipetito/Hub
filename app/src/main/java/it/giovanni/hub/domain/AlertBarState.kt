package it.giovanni.hub.domain

import androidx.compose.runtime.*

class AlertBarState {
    var alertSuccess by mutableStateOf<String?>(null)
        private set
    var alertException by mutableStateOf<Exception?>(null)
        private set
    var alertStringManager by mutableStateOf<StringManager?>(null)
        private set
    internal var updated by mutableStateOf(false)
        private set

    fun addSuccess(success: String) {
        alertException = null
        alertStringManager = null
        alertSuccess = success
        updated = !updated
    }

    fun addError(exception: Exception) {
        alertException = exception
        alertStringManager = null
        alertSuccess = null
        updated = !updated
    }

    fun addError(stringManager: StringManager) {
        alertException = null
        alertStringManager = stringManager
        alertSuccess = null
        updated = !updated
    }

    fun addError(throwable: Throwable) = addError(
        throwable as? Exception ?: Exception(throwable.message ?: throwable.toString(), throwable)
    )
}