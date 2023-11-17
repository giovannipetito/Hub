package it.giovanni.hub.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import it.giovanni.hub.ui.theme.Blue900
import it.giovanni.hub.ui.theme.LightBlue400

object Globals {

    /**
     * EXTENSIONS
     */

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
        get() = Color(android.graphics.Color.parseColor(this))

    val LazyListState.isScrolled: Boolean
        get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0
}