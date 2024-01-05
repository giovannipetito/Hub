package it.giovanni.hub.navigation.util.routes

sealed class AuthRoutes(val route: String) {

    data object Auth: AuthRoutes(route = "auth_screen")
    data object SignUp: AuthRoutes(route = "signup_screen")
}