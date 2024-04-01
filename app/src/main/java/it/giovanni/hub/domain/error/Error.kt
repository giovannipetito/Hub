package it.giovanni.hub.domain.error

sealed interface Error

typealias RootError = Error

enum class Network: Error {
    SERIALIZATION_ERROR,
    REQUEST_TIMEOUT,
    REQUESTS_ERROR,
    INTERNET_ERROR,
    PAYLOAD_ERROR,
    UNKNOWN_ERROR,
    SERVER_ERROR
}
enum class Local: Error {
    DISK_ERROR
}

enum class Password: Error {
    PASSWORD_TOO_SHORT,
    PASSWORD_TOO_LONG,
    NO_UPPERCASE,
    NO_DIGIT
}

/*
sealed interface DataError: Error {
    enum class Network: DataError {
        SERIALIZATION_ERROR,
        REQUEST_TIMEOUT,
        REQUESTS_ERROR,
        INTERNET_ERROR,
        PAYLOAD_ERROR,
        UNKNOWN_ERROR,
        SERVER_ERROR
    }
    enum class Local: DataError {
        DISK_ERROR
    }
}

sealed interface UserError: Error {
    enum class Password: UserError {
        PASSWORD_TOO_SHORT,
        PASSWORD_TOO_LONG,
        NO_UPPERCASE,
        NO_DIGIT
    }
}
*/