package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.routes.MainRoutes
import it.giovanni.hub.navigation.util.routes.SettingsRoutes
import it.giovanni.hub.presentation.screen.detail.AlertBarScreen
import it.giovanni.hub.presentation.screen.detail.HorizontalPagerScreen
import it.giovanni.hub.presentation.screen.detail.HubButtonsScreen
import it.giovanni.hub.presentation.screen.detail.HubCardsScreen
import it.giovanni.hub.presentation.screen.detail.HubChipsScreen
import it.giovanni.hub.presentation.screen.detail.HubColorsScreen
import it.giovanni.hub.presentation.screen.detail.HubColumnsScreen
import it.giovanni.hub.presentation.screen.detail.HubFontsScreen
import it.giovanni.hub.presentation.screen.detail.HubGridsScreen
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
        startDestination = MainRoutes.Settings.route
    ) {
        composable(
            route = MainRoutes.Settings.route
        ) {
            SettingsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Colors.route
        ) {
            HubColorsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Fonts.route
        ) {
            HubFontsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Buttons.route
        ) {
            HubButtonsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Columns.route
        ) {
            HubColumnsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Rows.route
        ) {
            HubRowsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Grids.route
        ) {
            HubGridsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Texts.route
        ) {
            HubTextsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.TextFields.route
        ) {
            HubTextFieldsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Cards.route
        ) {
            HubCardsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.UI.route
        ) {
            UIScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Slider.route
        ) {
            SliderScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.PhotoPicker.route
        ) {
            PhotoPickerScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Shimmer.route
        ) {
            ShimmerScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Shuffled.route
        ) {
            ShuffledScreen(navController = navController)
        }

        topBarsNavGraph(navController = navController)

        composable(
            route = SettingsRoutes.HorizontalPager.route
        ) {
            HorizontalPagerScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.ProgressIndicators.route
        ) {
            HubProgressIndicatorsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.Chips.route
        ) {
            HubChipsScreen(navController = navController)
        }

        composable(
            route = SettingsRoutes.AlertBar.route
        ) {
            AlertBarScreen(navController = navController)
        }
    }
}