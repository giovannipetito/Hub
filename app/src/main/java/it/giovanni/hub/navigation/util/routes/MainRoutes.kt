package it.giovanni.hub.navigation.util.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import it.giovanni.hub.R

enum class MainRoutes(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val iconResOn: Int,
    val iconResOff: Int,
) {
    Home(
        route = "home_screen",
        label = "Home",
        icon = Icons.Default.Home,
        iconResOn = R.drawable.ico_home_on,
        iconResOff = R.drawable.ico_home_off,
    ),
    Profile(
        route = "profile_screen",
        label = "Profile",
        icon = Icons.Default.Person,
        iconResOn = R.drawable.ico_profile_on,
        iconResOff = R.drawable.ico_profile_off,
    ),
    Settings(
        route = "settings_screen",
        label = "Settings",
        icon = Icons.Default.Settings,
        iconResOn = R.drawable.ico_settings_on,
        iconResOff = R.drawable.ico_settings_off,
    )
}