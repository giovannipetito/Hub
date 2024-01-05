package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.routes.SettingsRoutes
import it.giovanni.hub.ui.items.buttons.MainTextButton
import it.giovanni.hub.utils.Constants.STATUS_BAR_HEIGHT

@Composable
fun SettingsScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = STATUS_BAR_HEIGHT)
    ) {
        item {
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Colors.route)
                                     }, id = R.string.colors
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Fonts.route)
                                     }, id = R.string.fonts
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Buttons.route)
                                     }, id = R.string.buttons
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Columns.route)
                                     }, id = R.string.columns
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Rows.route)
                                     }, id = R.string.rows
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Grids.route)
            }, id = R.string.grids
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Texts.route)
                                     }, id = R.string.texts
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.TextFields.route)
                                     }, id = R.string.text_fields
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Cards.route)
                                     }, id = R.string.cards
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.UI.route)
                                     }, id = R.string.ui_components
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Slider.route)
                                     }, id = R.string.slider
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.PhotoPicker.route)
                                     }, id = R.string.photo_picker
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Shimmer.route)
                                     }, id = R.string.shimmer_items
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Shuffled.route)
                                     }, id = R.string.shuffled_items
            )
            MainTextButton(onClick = {
                navController.navigate(route = Graph.TOP_APPBARS_ROUTE)
                                     }, id = R.string.top_app_bars
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.HorizontalPager.route)
                                     }, id = R.string.horizontal_pager
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.ProgressIndicators.route)
                                     }, id = R.string.progress_indicators
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.Chips.route)
                                     }, id = R.string.chips
            )
            MainTextButton(onClick = {
                navController.navigate(route = SettingsRoutes.AlertBar.route)
                                     }, id = R.string.alert_bar
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}