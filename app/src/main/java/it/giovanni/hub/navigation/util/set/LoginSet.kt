package it.giovanni.hub.navigation.util.set

sealed class LoginSet(val route: String) {
    data object Login: LoginSet(route = "login_screen")
    data object Info: LoginSet(route = "info_screen")
}