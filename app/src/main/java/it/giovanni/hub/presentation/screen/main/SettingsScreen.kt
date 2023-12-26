package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.ui.items.buttons.MainTextButton

@Composable
fun SettingsScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 24.dp) // top = it.calculateTopPadding()
    ) {
        item {
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Colors.route)
                                     }, id = R.string.colors
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Fonts.route)
                                     }, id = R.string.fonts
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Buttons.route)
                                     }, id = R.string.buttons
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Columns.route)
                                     }, id = R.string.columns
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Rows.route)
                                     }, id = R.string.rows
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Texts.route)
                                     }, id = R.string.texts
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.TextFields.route)
                                     }, id = R.string.text_fields
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Cards.route)
                                     }, id = R.string.cards
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.UI.route)
                                     }, id = R.string.ui_components
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.PhotoPicker.route)
                                     }, id = R.string.photo_picker
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Shimmer.route)
                                     }, id = R.string.shimmer_items
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Shuffled.route)
                                     }, id = R.string.shuffled_items
            )
            MainTextButton(onClick = {
                navController.navigate(route = Graph.TOP_APPBARS_ROUTE)
                                     }, id = R.string.top_app_bars
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.HorizontalPager.route)
                                     }, id = R.string.horizontal_pager
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}