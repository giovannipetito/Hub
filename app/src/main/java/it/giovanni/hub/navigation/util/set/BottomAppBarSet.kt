package it.giovanni.hub.navigation.util.set

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomAppBarSet(
    val route: String,
    val contentDescription: String,
    val icon: ImageVector
) {
    Home(
        route = "home_screen",
        contentDescription = "Home",
        icon = Icons.Default.Home
    ),
    Profile(
        route = "profile_screen",
        contentDescription = "Profile",
        icon = Icons.Default.Person
    ),
    Settings(
        route = "settings_screen",
        contentDescription = "Settings",
        icon = Icons.Default.Settings
    )
}