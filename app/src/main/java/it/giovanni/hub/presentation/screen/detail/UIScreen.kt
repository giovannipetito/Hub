package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.MainActivity
import it.giovanni.hub.ui.items.Card2
import it.giovanni.hub.ui.items.CircularIndicator
import it.giovanni.hub.ui.items.rainbowColors

@Composable
fun UIScreen(navController: NavController, mainActivity: MainActivity) {

    val brush = remember { Brush.horizontalGradient(colors = rainbowColors) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brush),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "UI",
                color = Color.Blue,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Card2(title = "Expandable Card")

                var value by remember {
                    mutableStateOf(0)
                }
                CircularIndicator(
                    indicatorValue = value
                )

                TextField(
                    value = value.toString(),
                    onValueChange = { input ->
                            value = if (input.isNotEmpty()) {
                                input.toInt()
                            } else {
                                0
                            }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UIScreenPreview() {
    UIScreen(navController = rememberNavController(), mainActivity = MainActivity())
}