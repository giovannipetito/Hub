package it.giovanni.hub.navigation.set

sealed class LoginSet(val route: String) {

    object Login: LoginSet(route = "login_screen")

    object Info: LoginSet(route = "info_screen")
}