package it.giovanni.hub.ui.theme

import android.app.Activity
import android.os.Build
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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Material 3 light color scheme
private val hubLightColorScheme = lightColorScheme(
    primary = hubLightPrimary,
    onPrimary = hubLightOnPrimary,
    primaryContainer = hubLightPrimaryContainer,
    onPrimaryContainer = hubLightOnPrimaryContainer,
    secondary = hubLightSecondary,
    onSecondary = hubLightOnSecondary,
    secondaryContainer = hubLightSecondaryContainer,
    onSecondaryContainer = hubLightOnSecondaryContainer,
    tertiary = hubLightTertiary,
    onTertiary = hubLightOnTertiary,
    tertiaryContainer = hubLightTertiaryContainer,
    onTertiaryContainer = hubLightOnTertiaryContainer,
    error = hubLightError,
    onError = hubLightOnError,
    errorContainer = hubLightErrorContainer,
    onErrorContainer = hubLightOnErrorContainer,
    outline = hubLightOutline,
    background = hubLightBackground,
    onBackground = hubLightOnBackground,
    surface = hubLightSurface,
    onSurface = hubLightOnSurface,
    surfaceVariant = hubLightSurfaceVariant,
    onSurfaceVariant = hubLightOnSurfaceVariant,
    inverseSurface = hubLightInverseSurface,
    inverseOnSurface = hubLightInverseOnSurface,
    inversePrimary = hubLightInversePrimary,
    surfaceTint = hubLightSurfaceTint,
    outlineVariant = hubLightOutlineVariant,
    scrim = hubLightScrim,
)

// Material 3 dark color scheme
private val hubDarkColorScheme = darkColorScheme(
    primary = hubDarkPrimary,
    onPrimary = hubDarkOnPrimary,
    primaryContainer = hubDarkPrimaryContainer,
    onPrimaryContainer = hubDarkOnPrimaryContainer,
    secondary = hubDarkSecondary,
    onSecondary = hubDarkOnSecondary,
    secondaryContainer = hubDarkSecondaryContainer,
    onSecondaryContainer = hubDarkOnSecondaryContainer,
    tertiary = hubDarkTertiary,
    onTertiary = hubDarkOnTertiary,
    tertiaryContainer = hubDarkTertiaryContainer,
    onTertiaryContainer = hubDarkOnTertiaryContainer,
    error = hubDarkError,
    onError = hubDarkOnError,
    errorContainer = hubDarkErrorContainer,
    onErrorContainer = hubDarkOnErrorContainer,
    outline = hubDarkOutline,
    background = hubDarkBackground,
    onBackground = hubDarkOnBackground,
    surface = hubDarkSurface,
    onSurface = hubDarkOnSurface,
    surfaceVariant = hubDarkSurfaceVariant,
    onSurfaceVariant = hubDarkOnSurfaceVariant,
    inverseSurface = hubDarkInverseSurface,
    inverseOnSurface = hubDarkInverseOnSurface,
    inversePrimary = hubDarkInversePrimary,
    surfaceTint = hubDarkSurfaceTint,
    outlineVariant = hubDarkOutlineVariant,
    scrim = hubDarkScrim,
)

@Composable
fun HubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    val hubColorScheme: ColorScheme = when {
        dynamicColor /* && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S */ -> {
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
     * It handles the color of system bars (status bar and navigation bar).
     */
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = hubColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme

            // window.navigationBarColor = hubColorScheme.primary.toArgb()
            // WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = hubColorScheme,
        shapes = shapes,
        typography = typography, // Or: hubTypography
        content = content
    )
}