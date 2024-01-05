package it.giovanni.hub.domain

import androidx.compose.runtime.*

class AlertBarState {
    var success by mutableStateOf<String?>(null)
        private set
    var error by mutableStateOf<Exception?>(null)
        private set
    internal var updated by mutableStateOf(false)
        private set

    fun addSuccess(message: String) {
        error = null
        success = message
        updated = !updated
    }

    fun addError(exception: Exception) {
        success = null
        error = exception
        updated = !updated
    }
}