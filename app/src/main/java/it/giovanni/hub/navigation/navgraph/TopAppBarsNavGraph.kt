package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.TopAppBars
import it.giovanni.hub.navigation.util.routes.TopAppBarsRoutes
import it.giovanni.hub.presentation.screen.detail.topappbars.CenterAlignedTopAppBarScreen
import it.giovanni.hub.presentation.screen.detail.topappbars.CollapsingTopAppBarScreen
import it.giovanni.hub.presentation.screen.detail.topappbars.LargeTopAppBarScreen
import it.giovanni.hub.presentation.screen.detail.topappbars.MediumTopAppBarScreen
import it.giovanni.hub.presentation.screen.detail.topappbars.TopAppBarScreen
import it.giovanni.hub.presentation.screen.main.TopAppBarsScreen

fun NavGraphBuilder.topAppBarsNavGraph(navController: NavHostController) {
    navigation<TopAppBars>(
        startDestination = TopAppBarsRoutes.HubTopAppBars
    ) {
        composable<TopAppBarsRoutes.HubTopAppBars> {
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

        composable<TopAppBarsRoutes.HubCollapsingTopAppBar> {
            CollapsingTopAppBarScreen(navController = navController)
        }
    }
}