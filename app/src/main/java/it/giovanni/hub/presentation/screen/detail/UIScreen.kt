package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.MainActivity
import it.giovanni.hub.ui.items.cards.ExpandableCard
import it.giovanni.hub.ui.items.CircularIndicator
import it.giovanni.hub.ui.items.SelectableItem
import it.giovanni.hub.ui.items.rainbowColors

@Composable
fun UIScreen(navController: NavController, mainActivity: MainActivity) {

    val brush = remember { Brush.horizontalGradient(colors = rainbowColors) }

    var selected1 by remember { mutableStateOf(false) }
    var selected2 by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brush),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                ExpandableCard(
                    modifier = Modifier.padding(all = 12.dp),
                    title = "Expandable Card"
                )

                var value by remember {
                    mutableIntStateOf(0)
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

                Spacer(modifier = Modifier.height(12.dp))

                SelectableItem(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    selected = selected1,
                    title = "Selectable Item 1",
                    onClick = {
                        selected1 = !selected1
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SelectableItem(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    selected = selected2,
                    title = "Selectable Item 2",
                    subtitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.",
                    onClick = {
                        selected2 = !selected2
                    }
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