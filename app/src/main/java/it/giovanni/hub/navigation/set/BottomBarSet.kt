package it.giovanni.hub.navigation.set

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarSet(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home: BottomBarSet(
        route = "home_screen",
        label = "Home",
        icon = Icons.Default.Home
    )
    object Profile: BottomBarSet(
        route = "profile_screen",
        label = "Profile",
        icon = Icons.Default.Person
    )
    object Settings: BottomBarSet(
        route = "settings_screen",
        label = "Settings",
        icon = Icons.Default.Settings
    )
}