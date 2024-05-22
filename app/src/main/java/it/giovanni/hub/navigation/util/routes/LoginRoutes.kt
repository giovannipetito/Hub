package it.giovanni.hub.navigation.util.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class LoginRoutes(val route: String) {
    @Serializable
    data object Login: LoginRoutes(route = "login_screen")
    @Serializable
    data object Info: LoginRoutes("")
}