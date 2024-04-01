package it.giovanni.hub.domain.usecase

import it.giovanni.hub.domain.error.Error
import it.giovanni.hub.domain.result.pro.HubResultPro

interface PasswordValidator {
    suspend fun validatePassword(password: String): HubResultPro<Unit, Error>
}