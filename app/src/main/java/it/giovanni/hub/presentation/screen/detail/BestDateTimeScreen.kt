package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.memo.formatDayMonthForLocale
import it.giovanni.hub.domain.memo.formatForLocale
import it.giovanni.hub.domain.memo.formatForLocaleShort
import it.giovanni.hub.utils.Globals.getContentPadding
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

@Composable
fun BestDateTimeScreen(navController: NavController) {

    val topics: List<String> = listOf("BestDateTime")

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.best_date_time),
        topics = topics
    ) { paddingValues ->

        val rows = previewDateAndTimeLocales()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            /*
            items(rows) { row ->
                Text(
                    modifier= Modifier.padding(start = 12.dp),
                    text = row
                )
            }
            */

            rows.forEach { row ->
                item {
                    Text(
                        modifier= Modifier.padding(start = 12.dp),
                        text = row
                    )
                }
            }
        }
    }
}

fun previewDateAndTimeLocales(): List<String> {
    val date = LocalDate.of(2026, 2, 6)
    val time = LocalTime.of(14, 30)

    val locales = listOf(
        Locale.CANADA,
        Locale.CANADA_FRENCH,
        Locale.CHINA,
        Locale.CHINESE,
        Locale.ENGLISH,
        Locale.FRANCE,
        Locale.FRENCH,
        Locale.GERMAN,
        Locale.GERMANY,
        Locale.ITALIAN,
        Locale.ITALY,
        Locale.JAPAN,
        Locale.JAPANESE,
        Locale.KOREA,
        Locale.KOREAN,
        Locale.PRC,
        Locale.ROOT,
        Locale.SIMPLIFIED_CHINESE,
        Locale.TAIWAN,
        Locale.TRADITIONAL_CHINESE,
        Locale.UK,
        Locale.US,
        Locale("es", "ES"),
        Locale("pt", "BR"),
        Locale("nl", "NL"),
        Locale("tr", "TR"),
        Locale("ko", "KR"),
        Locale("zh", "CN"),
        Locale("ar", "SA"),
        Locale("ru", "RU"),
        Locale("hi", "IN")
    )

    return locales.map { locale ->
        val formattedDate = date.formatDayMonthForLocale(locale)
        val formattedTime = time.formatForLocale(locale)
        val formattedTimeShort = time.formatForLocaleShort(locale)
        "${locale.toLanguageTag()} -> $formattedDate - $formattedTime - $formattedTimeShort"
    }
}

@Preview(showBackground = true)
@Composable
fun BestDateTimeScreenPreview() {
    BestDateTimeScreen(navController = rememberNavController())
}