package it.giovanni.hub.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class LoginRoutes {
    @Serializable
    data object Login: LoginRoutes()
    @Serializable
    data object Info: LoginRoutes()
}