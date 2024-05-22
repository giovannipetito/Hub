package it.giovanni.hub.navigation.util.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoutes {
    @Serializable
    data object Auth: AuthRoutes()
    @Serializable
    data object SignUp: AuthRoutes()
}