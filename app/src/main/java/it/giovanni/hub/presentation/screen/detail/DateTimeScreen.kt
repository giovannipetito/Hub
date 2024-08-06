package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.utils.Globals.getContentPadding
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

data class City(
    val name: String,
    val timeZone: TimeZone
)

@Composable
fun DateTimeScreen(navController: NavController) {

    val topics: List<String> = listOf("DateTime")

    val cities = remember {
        listOf(
            City("Tokyo", TimeZone.of("Asia/Tokyo")),
            City("Berlin", TimeZone.of("Europe/Berlin")),
            City("London", TimeZone.of("Europe/London")),
            City("Sydney", TimeZone.of("Australia/Sydney")),
            City("New York", TimeZone.of("America/New_York")),
            City("Los Angeles", TimeZone.of("America/Los_Angeles"))
        )
    }

    var cityTimes by remember {
        mutableStateOf(listOf<Pair<City, LocalDateTime>>())
    }

    LaunchedEffect(true) {
        while (true) {
            cityTimes = cities.map {
                val now = Clock.System.now()
                it to now.toLocalDateTime(timeZone = it.timeZone)
            }
            delay(1000L)
        }
    }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.date_time),
        topics = topics
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            items(cityTimes) { (city, dateTime) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = city.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = dateTime
                                .format(
                                    LocalDateTime.Format {
                                        hour()
                                        char(':')
                                        minute()
                                        char(':')
                                        second()
                                    }
                                ),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            text = dateTime
                                .format(
                                    LocalDateTime.Format {
                                        dayOfMonth()
                                        char('/')
                                        monthNumber()
                                        char('/')
                                        year()
                                    }
                                ),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DateTimeScreenPreview() {
    DateTimeScreen(navController = rememberNavController())
}