package it.giovanni.hub.navigation.util.routes

sealed class LoginRoutes(val route: String) {

    data object Login: LoginRoutes(route = "login_screen")
    data object Info: LoginRoutes(route = "info_screen")
}