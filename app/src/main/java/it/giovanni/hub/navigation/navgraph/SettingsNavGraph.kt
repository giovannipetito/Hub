package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.presentation.screen.detail.CollapsingTopBarScreen
import it.giovanni.hub.presentation.screen.detail.ShimmerScreen
import it.giovanni.hub.presentation.screen.detail.ShuffledScreen
import it.giovanni.hub.presentation.screen.detail.TextFieldsScreen
import it.giovanni.hub.presentation.screen.detail.TopBarScreen
import it.giovanni.hub.presentation.screen.detail.UIScreen
import it.giovanni.hub.presentation.screen.main.SettingsScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    personViewModel: PersonViewModel
) {
    navigation(
        route = Graph.SETTINGS_ROUTE,
        startDestination = MainSet.Settings.route
    ) {
        composable(
            route = MainSet.Settings.route
        ) {
            SettingsScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable(
            route = MainSet.TextFields.route
        ) {
            TextFieldsScreen(navController = navController)
        }

        composable(
            route = MainSet.UI.route
        ) {
            UIScreen(navController = navController)
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

        composable(
            route = MainSet.TopBar.route
        ) {
            TopBarScreen(navController = navController)
        }

        composable(
            route = MainSet.CollapsingTopBar.route
        ) {
            CollapsingTopBarScreen(navController = navController)
        }
    }
}