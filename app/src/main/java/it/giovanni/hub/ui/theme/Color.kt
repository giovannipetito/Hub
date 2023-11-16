package it.giovanni.hub.ui.theme

import android.graphics.Color.parseColor
import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val LightBlue400 = Color(0xFF29B6FC)
val BlueGrey200 = Color(0xFFB0BBC5)
val LightBlue200 = Color(0xFF81D4fA)

val Blue900 = Color(0xFF0D47A1)
val BlueGrey800 = Color(0xFF37474F)
val Blue800 = Color(0xFF1565C0)

/**
 * Extension property color of androidx.compose.material.MaterialTheme.
 */
val Colors.hexColor: Color
    get() = if (isLight) LightBlue400 else Blue900

/**
 * We can use this extension property color when for example we try to fetch
 * some Hex color code from an API and we want to parse it directly to my UI.
 */
val String.hexColor
    get() = Color(parseColor(this))