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

        composable<TopAppBarsRoutes.HubTopAppBar> {
            TopAppBarScreen(navController = navController)
        }

        composable<TopAppBarsRoutes.HubCenterAlignedTopAppBar> {
            CenterAlignedTopAppBarScreen(navController = navController)
        }

        composable<TopAppBarsRoutes.HubMediumTopAppBar> {
            MediumTopAppBarScreen(navController = navController)
        }

        composable<TopAppBarsRoutes.HubLargeTopAppBar> {
            LargeTopAppBarScreen(navController = navController)
        }

        composable<TopAppBarsRoutes.HubSearchTopAppBar> {
            SearchTopAppBarScreen(navController = navController)
        }

        composable<TopAppBarsRoutes.HubCollapsingTopAppBar> {
            CollapsingTopAppBarScreen(navController = navController)
        }
    }
}