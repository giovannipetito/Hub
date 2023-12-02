package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.TopAppBarsSet
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
        startDestination = TopAppBarsSet.HubTopAppBars.route
    ) {
        composable(
            route = TopAppBarsSet.HubTopAppBars.route
        ) {
            TopAppBarsScreen(navController = navController)
        }

        composable(
            route = TopAppBarsSet.HubTopAppBar.route
        ) {
            TopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsSet.HubCenterAlignedTopAppBar.route
        ) {
            CenterAlignedTopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsSet.HubMediumTopAppBar.route
        ) {
            MediumTopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsSet.HubLargeTopAppBar.route
        ) {
            LargeTopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsSet.HubSearchTopAppBar.route
        ) {
            SearchTopAppBarScreen(navController = navController)
        }

        composable(
            route = TopAppBarsSet.HubCollapsingTopAppBar.route
        ) {
            CollapsingTopAppBarScreen(navController = navController)
        }
    }
}