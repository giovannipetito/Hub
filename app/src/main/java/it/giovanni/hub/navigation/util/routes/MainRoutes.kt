package it.giovanni.hub.navigation.util.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class MainRoutes(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    Home(
        route = "home_screen",
        label = "Home",
        icon = Icons.Default.Home
    ),
    Profile(
        route = "profile_screen",
        label = "Profile",
        icon = Icons.Default.Person
    ),
    Settings(
        route = "settings_screen",
        label = "Settings",
        icon = Icons.Default.Settings
    )
}