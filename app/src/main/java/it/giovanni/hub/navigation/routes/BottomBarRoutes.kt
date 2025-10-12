package it.giovanni.hub.navigation.routes

import it.giovanni.hub.R

enum class BottomBarRoutes(
    val route: String,
    val label: String,
    val iconOn: Int,
    val iconOff: Int,
) {
    Home(
        route = "home_screen",
        label = "Home",
        iconOn = R.drawable.ico_home_on,
        iconOff = R.drawable.ico_home_off,
    ),
    Profile(
        route = "profile_screen",
        label = "Profile",
        iconOn = R.drawable.ico_profile_on,
        iconOff = R.drawable.ico_profile_off,
    ),
    Settings(
        route = "settings_screen",
        label = "Settings",
        iconOn = R.drawable.ico_settings_on,
        iconOff = R.drawable.ico_settings_off,
    )
}