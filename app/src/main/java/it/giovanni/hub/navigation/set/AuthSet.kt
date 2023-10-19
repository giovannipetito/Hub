package it.giovanni.hub.navigation.set

sealed class AuthSet(val route: String) {

    object Auth: AuthSet(route = "auth_screen")

    object SignUp: AuthSet(route = "signup_screen")
}