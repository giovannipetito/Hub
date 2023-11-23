package it.giovanni.hub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val darkColorScheme = darkColorScheme(
    primary = Blue900,
    secondary = BlueGrey800,
    tertiary = Blue800
)

private val lightColorScheme = lightColorScheme(
    primary = LightBlue400,
    secondary = BlueGrey200,
    tertiary = LightBlue200
)
/*
Other default colors to override
background = Color.White,
surface = Color.White,
onPrimary = Color.White,
onSecondary = Color.Black,
onBackground = Color.Black,
onSurface = Color.Black,
*/

@Composable
fun HubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val hubColorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    /**
     * This code should be used to handle the color and transparency of the system bars if the app
     * display is not edge-to-edge.
     */
    /*
    val view = LocalView.current
    if (!view.isInEditMode) {
        val isSystemInLightTheme = !isSystemInDarkTheme()
        SideEffect {
            val window = (view.context as Activity).window
            if (isSystemInLightTheme) {
                window.statusBarColor = Color.WHITE
                window.navigationBarColor = Color.WHITE
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = darkTheme
            } else {
                window.statusBarColor = Color.BLACK
                window.navigationBarColor = Color.BLACK
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars
            }
        }
    }
    */

    MaterialTheme(
        colorScheme = hubColorScheme,
        shapes = shapes,
        typography = typography, // Or: HubTypography
        content = content
    )
}