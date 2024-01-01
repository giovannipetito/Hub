package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.ContinueSlider
import it.giovanni.hub.ui.items.DiscreteSlider
import it.giovanni.hub.ui.items.RangeContinueSlider
import it.giovanni.hub.ui.items.RangeDiscreteSlider
import it.giovanni.hub.ui.items.SliderIndicator
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun SliderScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "SliderIndicator",
        "ContinueSlider",
        "DiscreteSlider",
        "RangeContinueSlider",
        "RangeDiscreteSlider"
    )

    var sliderPosition by remember { mutableFloatStateOf(0f) }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.slider),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues)
        ) {
            item {
                SliderIndicator(
                    indicatorValue = sliderPosition.toInt()
                )

                ContinueSlider(
                    position = sliderPosition,
                    onValueChange = { newPosition ->
                        sliderPosition = newPosition
                    }
                )

                DiscreteSlider(
                    position = sliderPosition,
                    onValueChange = { newPosition ->
                        sliderPosition = newPosition
                    }
                )

                RangeContinueSlider()

                RangeDiscreteSlider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SliderScreenPreview() {
    SliderScreen(navController = rememberNavController())
}