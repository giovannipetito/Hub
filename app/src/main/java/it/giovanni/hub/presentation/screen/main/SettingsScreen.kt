package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.ui.items.MainText
import it.giovanni.hub.utils.Constants

@Composable
fun SettingsScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = Constants.BOTTOM_BAR_HEIGHT)
        ) {
            item {
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Texts.route)
                        },
                    text = "Texts"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.TextFields.route)
                        },
                    text = "TextFields"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Boxes.route)
                        },
                    text = "Boxes"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Columns.route)
                        },
                    text = "Columns"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Rows.route)
                        },
                    text = "Rows"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.UI.route)
                        },
                    text = "Custom components"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.PhotoPicker.route)
                        },
                    text = "PhotoPicker"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Shimmer.route)
                        },
                    text = "Shimmer Items"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Shuffled.route)
                        },
                    text = "Shuffled Items"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = Graph.TOP_APPBARS_ROUTE)
                        },
                    text = "TopAppBars"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.HorizontalPager.route)
                        },
                    text = "Horizontal Pager"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}