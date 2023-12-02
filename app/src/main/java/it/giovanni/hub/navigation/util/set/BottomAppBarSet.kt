package it.giovanni.hub.navigation.util.set

sealed class BottomAppBarSet(val route: String) {
    data object Home: BottomAppBarSet(route = "home_screen")
    data object Profile: BottomAppBarSet(route = "profile_screen")
    data object Settings: BottomAppBarSet(route = "settings_screen")
}