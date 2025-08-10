package it.giovanni.hub.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoutes {
    @Serializable
    data object Auth: AuthRoutes()
    @Serializable
    data object SignUp: AuthRoutes()
}