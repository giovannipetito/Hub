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

    // Smartphone in portrait mode  ---> screenHeightDp: 725, screenWidthDp: 360 ---> screenSize.height: Medium, screenSize.width: Small
    // Smartphone in landscape mode ---> screenHeightDp: 336, screenWidthDp: 725 ---> screenSize.height: Small, screenSize.width: Medium

    // Tablet in portrait mode  ---> screenSize.height: Large, screenSize.width: Medium
    // Tablet in landscape mode ---> screenSize.height: Medium, screenSize.width: Large

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

fun getScreenHeight(height: Int): ScreenType = when {
    height < 480 -> ScreenType.Small
    height < 900 -> ScreenType.Medium
    else -> ScreenType.Large
}

fun getScreenWidth(width: Int): ScreenType = when {
    width < 600 -> ScreenType.Small
    width < 840 -> ScreenType.Medium
    else -> ScreenType.Large
}