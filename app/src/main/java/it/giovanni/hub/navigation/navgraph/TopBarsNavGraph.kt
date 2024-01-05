package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.routes.TopAppBarsRoutes
import it.giovanni.hub.presentation.screen.detail.topappbars.CenterAlignedTopAppBarScreen
import it.giovanni.hub.presentation.screen.detail.topappbars.CollapsingTopAppBarScreen
import it.giovanni.hub.presentation.screen.detail.topappbars.LargeTopAppBarScreen
import it.giovanni.hub.presentation.screen.detail.topappbars.MediumTopAppBarScreen
import it.giovanni.hub.presentation.screen.detail.topappbars.SearchTopAppBarScreen
import it.giovanni.hub.presentation.screen.detail.topappbars.TopAppBarScreen
import it.giovanni.hub.presentation.screen.main.TopAppBarsScreen

fun NavGraphBuilder.topBarsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.TOP_APPBARS_ROUTE,
        startDestination = TopAppBarsRoutes.HubTopAppBars.route
    ) {
        composable(
            route = TopAppBarsRoutes.HubTopAppBars.route
        ) {
            TopAppBarsScreen(navController = navController)
        }

        composable(
            route = TopAppBarsRoutes.HubTopAppBar.route
        ) {
            TopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsRoutes.HubCenterAlignedTopAppBar.route
        ) {
            CenterAlignedTopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsRoutes.HubMediumTopAppBar.route
        ) {
            MediumTopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsRoutes.HubLargeTopAppBar.route
        ) {
            LargeTopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsRoutes.HubSearchTopAppBar.route
        ) {
            SearchTopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsRoutes.HubCollapsingTopAppBar.route
        ) {
            CollapsingTopAppBarScreen(navController = navController)
        }
    }
}