package it.giovanni.hub.ui.items

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import it.giovanni.hub.utils.ScreenType

data class ScreenSize(
    val width: ScreenType,
    val height: ScreenType
)

@Composable
fun rememberScreenSize(): ScreenSize {

    val configuration = LocalConfiguration.current
    val screenWidth = remember(key1 = configuration) {
        mutableIntStateOf(configuration.screenWidthDp)
    }
    val screenHeight = remember(key1 = configuration) {
        mutableIntStateOf(configuration.screenHeightDp)
    }

    return ScreenSize(
        width = getScreenWidth(screenWidth.intValue),
        height = getScreenHeight(screenHeight.intValue)
    )
}

fun getScreenWidth(width: Int): ScreenType = when {
    width < 600 -> ScreenType.Compact
    width < 840 -> ScreenType.Medium
    else -> ScreenType.Expanded
}

fun getScreenHeight(height: Int): ScreenType = when {
    height < 480 -> ScreenType.Compact
    height < 900 -> ScreenType.Medium
    else -> ScreenType.Expanded
}