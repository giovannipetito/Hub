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
import it.giovanni.hub.R
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
                    id = R.string.texts
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.TextFields.route)
                        },
                    id = R.string.text_fields
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Boxes.route)
                        },
                    id = R.string.boxes
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Columns.route)
                        },
                    id = R.string.columns
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Rows.route)
                        },
                    id = R.string.rows
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.UI.route)
                        },
                    id = R.string.ui_components
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.PhotoPicker.route)
                        },
                    id = R.string.photo_picker
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Shimmer.route)
                        },
                    id = R.string.shimmer_items
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Shuffled.route)
                        },
                    id = R.string.shuffled_items
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = Graph.TOP_APPBARS_ROUTE)
                        },
                    id = R.string.top_app_bars
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.HorizontalPager.route)
                        },
                    id = R.string.horizontal_pager
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