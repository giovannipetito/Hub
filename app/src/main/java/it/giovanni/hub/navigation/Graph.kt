package it.giovanni.hub.navigation

import kotlinx.serialization.Serializable

object Graph {

    // NavGraph routes
    const val HOME_ROUTE = "home"
    const val PROFILE_ROUTE = "profile"
    const val SETTINGS_ROUTE = "settings"
}

@Serializable
object Loading

@Serializable
object Wizard

@Serializable
object Login

@Serializable
object Home

@Serializable
object Profile

@Serializable
object Settings

@Serializable
object Auth

@Serializable
object TopAppBars