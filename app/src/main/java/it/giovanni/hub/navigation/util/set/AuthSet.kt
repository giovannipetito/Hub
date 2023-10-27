package it.giovanni.hub.navigation.util.set

sealed class AuthSet(val route: String) {

    data object Auth: AuthSet(route = "auth_screen")

    data object SignUp: AuthSet(route = "signup_screen")
}