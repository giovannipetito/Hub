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
import it.giovanni.hub.navigation.TopAppBars
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
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Colors)
                          },
                id = R.string.colors
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Fonts)
                          },
                id = R.string.fonts
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Buttons)
                          },
                id = R.string.buttons
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Columns)
                          },
                id = R.string.columns
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Rows)
                          },
                id = R.string.rows
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Grids)
                          },
                id = R.string.grids
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Texts)
                          },
                id = R.string.texts
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.TextFields)
                          },
                id = R.string.text_fields
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Cards)
                          },
                id = R.string.cards
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.UI)
                          },
                id = R.string.ui_components
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Slider)
                          },
                id = R.string.slider
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.PhotoPicker)
                          },
                id = R.string.photo_picker
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Shimmer)
                          },
                id = R.string.shimmer_items
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Shuffled)
                          },
                id = R.string.shuffled_items
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = TopAppBars)
                          },
                id = R.string.top_app_bars
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.HorizontalPager)
                          },
                id = R.string.horizontal_pager
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.ProgressIndicators)
                          },
                id = R.string.progress_indicators
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.Chips)
                          },
                id = R.string.chips
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.AlertBar)
                          },
                id = R.string.alert_bar
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.TextToSpeech)
                },
                id = R.string.text_to_speech
            )
            MainTextButton(
                onClick = {
                    navController.navigate(route = SettingsRoutes.SpeechToText)
                },
                id = R.string.speech_to_text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}