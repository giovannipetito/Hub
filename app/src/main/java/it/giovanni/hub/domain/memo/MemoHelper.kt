package it.giovanni.hub.domain.memo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun rememberDeviceLocale(): java.util.Locale {
    val config = androidx.compose.ui.platform.LocalConfiguration.current
    return remember(config) {
        config.locales[0] ?: java.util.Locale.getDefault()
    }
}

fun formatMemoDate(
    month: Int,
    day: Int,
    locale: Locale = Locale.getDefault()
): String {
    val date = LocalDate.of(LocalDate.now().year, month, day)
    val pattern = when (locale.language) {
        "en" -> "MMMM d"
        else -> "d MMMM"
    }
    return date.format(
        DateTimeFormatter.ofPattern(pattern, locale)
    ).replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase(locale) else char.toString()
    }
}