package it.giovanni.hub.domain.usecase

import it.giovanni.hub.domain.error.Error
import it.giovanni.hub.domain.error.Password
import it.giovanni.hub.domain.result.pro.HubResultPro
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordValidatorImpl @Inject constructor(): PasswordValidator {
    override suspend fun validatePassword(password: String): HubResultPro<Unit, Error> {
        if (password.length < 9) {
            return HubResultPro.Error(Password.PASSWORD_TOO_SHORT)
        }
        if (password.length > 20) {
            return HubResultPro.Error(Password.PASSWORD_TOO_LONG)
        }
        val isUpperCase = password.any { it.isUpperCase() }
        if (!isUpperCase) {
            return HubResultPro.Error(Password.NO_UPPERCASE)
        }
        val isDigit = password.any { it.isDigit() }
        if (!isDigit) {
            return HubResultPro.Error(Password.NO_DIGIT)
        }
        return HubResultPro.Success(Unit)
    }
}