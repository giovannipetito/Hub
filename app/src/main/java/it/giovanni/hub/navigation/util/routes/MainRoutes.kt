package it.giovanni.hub.navigation.util.routes

import it.giovanni.hub.navigation.util.entries.BottomAppBarEntries

sealed class MainRoutes(val route: String) {

    data object Home: MainRoutes(route = BottomAppBarEntries.Home.route)
    data object Profile: MainRoutes(route = BottomAppBarEntries.Profile.route)
    data object Settings: MainRoutes(route = BottomAppBarEntries.Settings.route)
}