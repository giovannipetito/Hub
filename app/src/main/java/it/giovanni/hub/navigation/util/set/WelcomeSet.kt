package it.giovanni.hub.navigation.util.set

sealed class WelcomeSet(val route: String) {
    data object Pager: WelcomeSet(route = "pager_screen")
    data object Login: WelcomeSet(route = "login_screen")
    data object Info: WelcomeSet(route = "info_screen")
}