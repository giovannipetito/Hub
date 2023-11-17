package it.giovanni.hub.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

object Constants {

    // Argument key names
    const val DETAIL_ARG_KEY1: String = "id"
    const val DETAIL_ARG_KEY2: String = "name"

    val TOP_BAR_HEIGHT = 56.dp
    val BOTTOM_BAR_HEIGHT = 56.dp

    val icons: List<ImageVector> = listOf(
        Icons.Default.Home,
        Icons.Default.Face,
        Icons.Default.Email,
        Icons.Default.List,
        Icons.Default.Check,
        Icons.Default.Edit
    )
}