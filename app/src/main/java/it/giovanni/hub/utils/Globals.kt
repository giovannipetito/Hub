package it.giovanni.hub.utils

import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Colors
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import it.giovanni.hub.ui.theme.hubDarkPrimary
import it.giovanni.hub.ui.theme.hubLightPrimary

object Globals {

    /**
     * EXTENSIONS
     */

    /**
     * Extension property color of androidx.compose.material.MaterialTheme.
     */
    val Colors.hexColor: Color
        get() = if (isLight) hubLightPrimary else hubDarkPrimary

    /**
     * We can use this extension property color when for example we try to fetch
     * some Hex color code from an API and we want to parse it directly to my UI.
     */
    val String.hexColor
        get() = Color(android.graphics.Color.parseColor(this))

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
}