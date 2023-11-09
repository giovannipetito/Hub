package it.giovanni.hub.ui.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import it.giovanni.hub.utils.WindowType

data class WindowSize(
    val width: WindowType,
    val height: WindowType
)

@Composable
fun rememberWindowSize(): WindowSize {

    val configuration = LocalConfiguration.current
    val screenWidth = remember(key1 = configuration) {
        mutableIntStateOf(configuration.screenWidthDp)
    }
    val screenHeight = remember(key1 = configuration) {
        mutableIntStateOf(configuration.screenHeightDp)
    }

    return WindowSize(
        width = getScreenWidth(screenWidth.intValue),
        height = getScreenHeight(screenHeight.intValue)
    )
}

fun getScreenWidth(width: Int): WindowType = when {
    width < 600 -> WindowType.Compact
    width < 840 -> WindowType.Medium
    else -> WindowType.Expanded
}

fun getScreenHeight(height: Int): WindowType = when {
    height < 480 -> WindowType.Compact
    height < 900 -> WindowType.Medium
    else -> WindowType.Expanded
}