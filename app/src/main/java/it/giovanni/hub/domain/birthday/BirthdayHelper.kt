package it.giovanni.hub.domain.birthday

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberDeviceLocale(): java.util.Locale {
    val config = androidx.compose.ui.platform.LocalConfiguration.current
    return remember(config) {
        config.locales[0] ?: java.util.Locale.getDefault()
    }
}