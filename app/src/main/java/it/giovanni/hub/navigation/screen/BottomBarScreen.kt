package it.giovanni.hub.navigation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreen(
        route = "home_screen",
        label = "Home",
        icon = Icons.Default.Home
    )
    object Profile: BottomBarScreen(
        route = "profile_screen",
        label = "Profile",
        icon = Icons.Default.Person
    )
    object Settings: BottomBarScreen(
        route = "settings_screen",
        label = "Settings",
        icon = Icons.Default.Settings
    )
}