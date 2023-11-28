package it.giovanni.hub.utils

import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Colors
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import it.giovanni.hub.ui.theme.md_theme_dark_primary
import it.giovanni.hub.ui.theme.md_theme_light_primary

object Globals {

    val brushRainbowColors: List<Color> = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta
    )

    @Composable
    fun getBrushLoginColors(): List<Color> {
        return listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.onPrimary,
            MaterialTheme.colorScheme.primaryContainer
        )
    }

    /**
     * Extension property color of androidx.compose.material.MaterialTheme.
     */
    val Colors.hexColor: Color
        get() = if (isLight) md_theme_light_primary else md_theme_dark_primary

    /**
     * We can use this extension property color when for example we try to fetch
     * some Hex color code from an API and we want to parse it directly to my UI.
     */
    val String.hexColor
        get() = Color(android.graphics.Color.parseColor(this))

    /**
     * If the display is edge-to-edge, some of the views can appear behind the system bars, such as
     * the BottomBar behind the navigation bar. The getStatusBarPadding and getNavigationBarPadding
     * methods handle overlaps using insets.
     */
    @Composable
    fun getNavigationBarPadding(): Dp {
        return BottomAppBarDefaults.windowInsets.asPaddingValues().calculateBottomPadding()
    }

    @Composable
    fun getStatusBarPadding(): Dp {
        return BottomAppBarDefaults.windowInsets.asPaddingValues().calculateTopPadding()
    }

    val LazyListState.isScrolled: Boolean
        get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

    fun formatTime(seconds: String, minutes: String, hours: String): String {
        return "$hours:$minutes:$seconds"
    }

    fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }
}