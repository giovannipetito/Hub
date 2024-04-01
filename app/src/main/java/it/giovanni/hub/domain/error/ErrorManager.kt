package it.giovanni.hub.domain.error

import it.giovanni.hub.R
import it.giovanni.hub.domain.StringManager

fun Error.returnErrorMessage(): StringManager {
    return when (this) {
        // Network
        Network.SERIALIZATION_ERROR -> StringManager.ResourceString(R.string.serialization_error)
        Network.REQUEST_TIMEOUT -> StringManager.ResourceString(R.string.request_timeout)
        Network.REQUESTS_ERROR -> StringManager.ResourceString(R.string.requests_error)
        Network.INTERNET_ERROR -> StringManager.ResourceString(R.string.internet_error)
        Network.PAYLOAD_ERROR -> StringManager.ResourceString(R.string.payload_error)
        Network.UNKNOWN_ERROR -> StringManager.ResourceString(R.string.unknown_error)
        Network.SERVER_ERROR -> StringManager.ResourceString(R.string.server_error)

        // Local
        Local.DISK_ERROR -> StringManager.ResourceString(R.string.disk_error)

        // Password
        Password.PASSWORD_TOO_SHORT -> StringManager.ResourceString(R.string.password_too_short_error)
        Password.PASSWORD_TOO_LONG -> StringManager.ResourceString(R.string.password_too_long_error)
        Password.NO_UPPERCASE -> StringManager.ResourceString(R.string.no_uppercase_error)
        Password.NO_DIGIT -> StringManager.ResourceString(R.string.no_digit_error)
        /*
        else -> {
            StringManager.ResourceString(R.string.generic_error)
        }
        */
    }
}