package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.presentation.screen.detail.HorizontalPagerScreen
import it.giovanni.hub.presentation.screen.detail.HubButtonsScreen
import it.giovanni.hub.presentation.screen.detail.HubCardsScreen
import it.giovanni.hub.presentation.screen.detail.HubChipsScreen
import it.giovanni.hub.presentation.screen.detail.HubColorsScreen
import it.giovanni.hub.presentation.screen.detail.HubColumnsScreen
import it.giovanni.hub.presentation.screen.detail.HubFontsScreen
import it.giovanni.hub.presentation.screen.detail.HubProgressIndicatorsScreen
import it.giovanni.hub.presentation.screen.detail.HubRowsScreen
import it.giovanni.hub.presentation.screen.detail.HubTextsScreen
import it.giovanni.hub.presentation.screen.detail.ShimmerScreen
import it.giovanni.hub.presentation.screen.detail.ShuffledScreen
import it.giovanni.hub.presentation.screen.detail.HubTextFieldsScreen
import it.giovanni.hub.presentation.screen.detail.PhotoPickerScreen
import it.giovanni.hub.presentation.screen.detail.SliderScreen
import it.giovanni.hub.presentation.screen.detail.UIScreen
import it.giovanni.hub.presentation.screen.main.SettingsScreen

fun NavGraphBuilder.settingsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.SETTINGS_ROUTE,
        startDestination = MainSet.Settings.route
    ) {
        composable(
            route = MainSet.Settings.route
        ) {
            SettingsScreen(navController = navController)
        }

        composable(
            route = MainSet.Colors.route
        ) {
            HubColorsScreen(navController = navController)
        }

        composable(
            route = MainSet.Fonts.route
        ) {
            HubFontsScreen(navController = navController)
        }

        composable(
            route = MainSet.Buttons.route
        ) {
            HubButtonsScreen(navController = navController)
        }

        composable(
            route = MainSet.Columns.route
        ) {
            HubColumnsScreen(navController = navController)
        }

        composable(
            route = MainSet.Rows.route
        ) {
            HubRowsScreen(navController = navController)
        }

        composable(
            route = MainSet.Texts.route
        ) {
            HubTextsScreen(navController = navController)
        }

        composable(
            route = MainSet.TextFields.route
        ) {
            HubTextFieldsScreen(navController = navController)
        }

        composable(
            route = MainSet.Cards.route
        ) {
            HubCardsScreen(navController = navController)
        }

        composable(
            route = MainSet.UI.route
        ) {
            UIScreen(navController = navController)
        }

        composable(
            route = MainSet.Slider.route
        ) {
            SliderScreen(navController = navController)
        }

        composable(
            route = MainSet.PhotoPicker.route
        ) {
            PhotoPickerScreen(navController = navController)
        }

        composable(
            route = MainSet.Shimmer.route
        ) {
            ShimmerScreen(navController = navController)
        }

        composable(
            route = MainSet.Shuffled.route
        ) {
            ShuffledScreen(navController = navController)
        }

        topBarsNavGraph(navController = navController)

        composable(
            route = MainSet.HorizontalPager.route
        ) {
            HorizontalPagerScreen(navController = navController)
        }

        composable(
            route = MainSet.ProgressIndicators.route
        ) {
            HubProgressIndicatorsScreen(navController = navController)
        }

        composable(
            route = MainSet.Chips.route
        ) {
            HubChipsScreen(navController = navController)
        }
    }
}