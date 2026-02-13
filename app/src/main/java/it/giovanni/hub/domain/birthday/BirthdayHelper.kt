package it.giovanni.hub.domain.birthday

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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