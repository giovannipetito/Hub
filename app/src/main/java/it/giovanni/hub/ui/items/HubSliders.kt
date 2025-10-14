package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.util.toRange

@Composable
fun ContinueSlider(position: Float, onValueChange: (Float) -> Unit) {
    var sliderPosition: Float by remember { mutableFloatStateOf(position) }
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Slider(
            value = sliderPosition,
            onValueChange = {
                onValueChange(it)
                sliderPosition = it
            },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.tertiary,
                activeTrackColor = MaterialTheme.colorScheme.tertiary,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
            onValueChangeFinished = {
                // Launch some business logic update with the state you hold.
            }
        )
        Text(text = sliderPosition.toInt().toString())
    }
}

@Composable
fun DiscreteSlider(position: Float, onValueChange: (Float) -> Unit) {
    var sliderPosition: Float by remember { mutableFloatStateOf(position) }
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Slider(
            value = sliderPosition,
            onValueChange = {
                onValueChange(it)
                sliderPosition = it
            },
            valueRange = 0f..100f,
            steps = 9,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.tertiary,
                activeTrackColor = MaterialTheme.colorScheme.tertiary,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
            onValueChangeFinished = {}
        )
        Text(text = sliderPosition.toInt().toString())
    }
}

@Composable
fun RangeContinueSlider() {
    var sliderPosition: ClosedFloatingPointRange<Float> by remember { mutableStateOf(0f..100f) }
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        RangeSlider(
            value = sliderPosition,
            onValueChange = { range -> sliderPosition = range },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.tertiary,
                activeTrackColor = MaterialTheme.colorScheme.tertiary,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
            onValueChangeFinished = {}
        )
        Text(text = sliderPosition.toString())
    }
}

@Composable
fun RangeDiscreteSlider() {
    var sliderPosition: ClosedFloatingPointRange<Float> by remember { mutableStateOf(0f..100f) }
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        RangeSlider(
            value = sliderPosition,
            onValueChange = { range -> sliderPosition = range },
            valueRange = 0f..100f,
            steps = 9,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.tertiary,
                activeTrackColor = MaterialTheme.colorScheme.tertiary,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
            onValueChangeFinished = {}
        )
        Text(text = sliderPosition.toRange().toString())
    }
}