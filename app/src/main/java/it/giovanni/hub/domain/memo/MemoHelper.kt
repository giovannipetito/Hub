package it.giovanni.hub.domain.memo

import android.content.ContentResolver
import android.provider.CalendarContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import it.giovanni.hub.data.entity.MemoEntity
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
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

fun isAllDayMemo(memo: MemoEntity): Boolean {
    return !memo.isBirthday && memo.time == "ALL_DAY"
}

fun isSingleTimeMemo(memo: MemoEntity): Boolean {
    return !memo.isBirthday &&
            memo.time.isNotBlank() &&
            memo.time != "ALL_DAY" &&
            !memo.time.contains("-")
}

fun isRangeTimeMemo(memo: MemoEntity): Boolean {
    return !memo.isBirthday && memo.time.contains("-")
}

fun parseHourMinute(value: String): Pair<Int, Int>? {
    val parts = value.split(":")
    if (parts.size != 2) return null

    val hour = parts[0].toIntOrNull() ?: return null
    val minute = parts[1].toIntOrNull() ?: return null

    if (hour !in 0..23 || minute !in 0..59) return null
    return hour to minute
}

fun buildLocalDateTimeMillis(
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int
): Long {
    return LocalDate.of(year, month, day)
        .atTime(hour, minute)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

fun formatTime(hour: Int, minute: Int): String {
    return "%02d:%02d".format(hour, minute)
}

fun getCurrentTimeString(): String {
    val now = LocalTime.now()
    return formatTime(now.hour, now.minute)
}

fun getEventRecurrenceRule(eventId: Long,  resolver: ContentResolver): String? {
    val projection = arrayOf(CalendarContract.Events.RRULE)
    val selection = "${CalendarContract.Events._ID} = ?"
    val selectionArgs = arrayOf(eventId.toString())

    resolver.query(
        CalendarContract.Events.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(CalendarContract.Events.RRULE)
            return cursor.getString(index)
        }
    }

    return null
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
    return if (time.isBlank() || time == "ALL_DAY") formattedTime else formattedTime.plus(" - $time")
}