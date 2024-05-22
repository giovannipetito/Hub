package it.giovanni.hub.navigation.util.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthRoutes(val route: String) {
    @Serializable
    data object Auth: AuthRoutes(route = "auth_screen")
    @Serializable
    data object SignUp: AuthRoutes("")
}