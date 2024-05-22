package it.giovanni.hub.navigation.util.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class LoginRoutes {
    @Serializable
    data object Login: LoginRoutes()
    @Serializable
    data object Info: LoginRoutes()
}