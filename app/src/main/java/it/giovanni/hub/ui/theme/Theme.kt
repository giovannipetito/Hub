package it.giovanni.hub.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Material 3 dark color scheme
private val hubDarkColorScheme = darkColorScheme(
    primary = hubDarkPrimary,
    onPrimary = hubDarkOnPrimary,
    primaryContainer = hubDarkPrimaryContainer,
    onPrimaryContainer = hubDarkOnPrimaryContainer,
    inversePrimary = hubDarkInversePrimary,
    secondary = hubDarkSecondary,
    onSecondary = hubDarkOnSecondary,
    secondaryContainer = hubDarkSecondaryContainer,
    onSecondaryContainer = hubDarkOnSecondaryContainer,
    tertiary = hubDarkTertiary,
    onTertiary = hubDarkOnTertiary,
    tertiaryContainer = hubDarkTertiaryContainer,
    onTertiaryContainer = hubDarkOnTertiaryContainer,
    background = hubDarkBackground,
    onBackground = hubDarkOnBackground,
    surface = hubDarkSurface,
    onSurface = hubDarkOnSurface,
    surfaceVariant = hubDarkSurfaceVariant,
    onSurfaceVariant = hubDarkOnSurfaceVariant,
    // surfaceTint = hubDarkSurfaceTint,
    inverseSurface = hubDarkInverseSurface,
    inverseOnSurface = hubDarkInverseOnSurface,
    error = hubDarkError,
    onError = hubDarkOnError,
    errorContainer = hubDarkErrorContainer,
    onErrorContainer = hubDarkOnErrorContainer,
    outline = hubDarkOutline,
    // outlineVariant = hubDarkOutlineVariant,
    // scrim = hubDarkScrim,
)

// Material 3 light color scheme
private val hubLightColorScheme = lightColorScheme(
    primary = hubLightPrimary,
    onPrimary = hubLightOnPrimary,
    primaryContainer = hubLightPrimaryContainer,
    onPrimaryContainer = hubLightOnPrimaryContainer,
    inversePrimary = hubLightInversePrimary,
    secondary = hubLightSecondary,
    onSecondary = hubLightOnSecondary,
    secondaryContainer = hubLightSecondaryContainer,
    onSecondaryContainer = hubLightOnSecondaryContainer,
    tertiary = hubLightTertiary,
    onTertiary = hubLightOnTertiary,
    tertiaryContainer = hubLightTertiaryContainer,
    onTertiaryContainer = hubLightOnTertiaryContainer,
    background = hubLightBackground,
    onBackground = hubLightOnBackground,
    surface = hubLightSurface,
    onSurface = hubLightOnSurface,
    surfaceVariant = hubLightSurfaceVariant,
    onSurfaceVariant = hubLightOnSurfaceVariant,
    // surfaceTint = hubLightSurfaceTint,
    inverseSurface = hubLightInverseSurface,
    inverseOnSurface = hubLightInverseOnSurface,
    error = hubLightError,
    onError = hubLightOnError,
    errorContainer = hubLightErrorContainer,
    onErrorContainer = hubLightOnErrorContainer,
    outline = hubLightOutline,
    // outlineVariant = hubLightOutlineVariant,
    // scrim = hubLightScrim,
)

@Composable
fun HubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    val hubColorScheme: ColorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme)
                dynamicDarkColorScheme(context)
            else
                dynamicLightColorScheme(context)
        }
        darkTheme -> hubDarkColorScheme
        else -> hubLightColorScheme
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
        typography = typography, // Or: hubTypography
        content = content
    )
}