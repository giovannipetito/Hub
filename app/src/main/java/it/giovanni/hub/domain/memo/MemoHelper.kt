package it.giovanni.hub.domain.memo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class MemoKind {
    GENERIC_EVENT,
    BIRTHDAY
}

enum class TimeMode {
    ALL_DAY,
    SINGLE_TIME,
    TIME_RANGE
}

enum class TimePickerTarget {
    SINGLE,
    START,
    END
}

fun formatTime(hour: Int, minute: Int): String {
    return "%02d:%02d".format(hour, minute)
}

@Composable
fun rememberDeviceLocale(): Locale {
    val config = androidx.compose.ui.platform.LocalConfiguration.current
    return remember(config) {
        config.locales[0] ?: Locale.getDefault()
    }
}

fun formatMemoDate(
    month: Int,
    day: Int,
    time: String,
    locale: Locale = Locale.getDefault()
): String {
    val date = LocalDate.of(LocalDate.now().year, month, day)
    val pattern = when (locale.language) {
        "en" -> "MMMM d"
        else -> "d MMMM"
    }
    val formattedTime = date.format(
        DateTimeFormatter.ofPattern(pattern, locale)
    ).replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase(locale) else char.toString()
    }
    return if (time.isNotBlank()) formattedTime.plus(" - $time") else formattedTime
}