package it.giovanni.hub.domain.birthday

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import it.giovanni.hub.presentation.screen.detail.CalendarEntry
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.collections.forEach
import kotlin.text.isDigit

// Is Date of Birth valid or Blank digits
fun isDobValidOrBlankDigits(digits: String): Boolean {
    return digits.isBlank() || digits.length == 8
}

// Date of Birth Visual Transformation
class DobVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(8)
        val transformed = formatDobDigits(digits)

        val hasFirstSlash = digits.length >= 2
        val hasSecondSlash = digits.length >= 4
        val transformedLen = transformed.length

        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                val o = offset.coerceIn(0, digits.length)

                var t = o
                // Slash appears after 2 digits -> index 2 in transformed
                if (hasFirstSlash && o >= 2) t += 1
                // Second slash appears after 4 digits -> index 5 in transformed
                if (hasSecondSlash && o >= 4) t += 1

                return t.coerceIn(0, transformedLen)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val t = offset.coerceIn(0, transformedLen)

                var o = t
                // If cursor is after first slash (index 2), subtract 1
                if (hasFirstSlash && t > 2) o -= 1
                // If cursor is after second slash (index 5), subtract 1
                if (hasSecondSlash && t > 5) o -= 1

                return o.coerceIn(0, digits.length)
            }
        }

        return TransformedText(AnnotatedString(transformed), offsetMapping)
    }
}

// Format Date of Birth digits
fun formatDobDigits(text: String): String {
    // keep only digits (even if keyboard shows letters, they won't be accepted)
    val digits = text.filter { it.isDigit() }.take(8) // ddMMyyyy
    // val d = digits.take(8)
    val dd = digits.take(2)
    val mm = digits.drop(2).take(2)
    val yyyy = digits.drop(4).take(4)

    return buildString {
        append(dd)
        if (digits.length >= 2) append("/")
        append(mm)
        if (digits.length >= 4) append("/")
        append(yyyy)
    }
}

fun buildYearEntries(
    year: Int,
    locale: Locale,
    weekStartsOn: DayOfWeek
): List<CalendarEntry> {
    fun monthTitle(month: Int): String {
        val name = YearMonth.of(year, month).month.getDisplayName(TextStyle.FULL, locale)
        return name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    }

    fun orderedWeekdays(): List<DayOfWeek> =
        (0..6).map { weekStartsOn.plus(it.toLong()) }

    fun dowIndex(d: DayOfWeek): Int {
        val raw = (d.value - weekStartsOn.value) % 7
        return if (raw < 0) raw + 7 else raw
    }

    val out = ArrayList<CalendarEntry>(600)

    for (month in 1..12) {
        out.add(CalendarEntry.MonthHeader(year, month, monthTitle(month)))

        val wds = orderedWeekdays()
        wds.forEach { dow ->
            out.add(
                CalendarEntry.WeekdayLabel(
                    text = dow.getDisplayName(TextStyle.NARROW, locale),
                    key = "wd-$year-$month-${dow.value}"
                )
            )
        }

        val ym = YearMonth.of(year, month)
        val firstDow = ym.atDay(1).dayOfWeek
        val leading = dowIndex(firstDow)
        val daysInMonth = ym.lengthOfMonth()
        val total = leading + daysInMonth
        val trailing = (7 - (total % 7)) % 7

        repeat(leading) { i ->
            out.add(CalendarEntry.Day(year, month, null, key = "d-$year-$month-lead-$i"))
        }

        for (d in 1..daysInMonth) {
            out.add(CalendarEntry.Day(year, month, d, key = "d-$year-$month-$d"))
        }

        repeat(trailing) { i ->
            out.add(CalendarEntry.Day(year, month, null, key = "d-$year-$month-trail-$i"))
        }

        out.add(CalendarEntry.Spacer(key = "sp-$year-$month"))
    }

    return out
}