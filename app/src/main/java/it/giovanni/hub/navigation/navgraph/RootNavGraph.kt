package it.giovanni.hub.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.Graph.MAIN_ROUTE
import it.giovanni.hub.Graph.WIZARD_ROUTE
import it.giovanni.hub.Graph.ROOT_ROUTE
import it.giovanni.hub.Graph.LOADING_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.presentation.screen.main.MainScreen
import it.giovanni.hub.presentation.screen.main.LoadingScreen
import it.giovanni.hub.presentation.screen.main.WizardScreen

@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainActivity: MainActivity
) {

    // Root Navigation Graph
    NavHost(
        navController = navController,
        route = ROOT_ROUTE,
        startDestination = LOADING_ROUTE
    ) {
        composable(route = LOADING_ROUTE) {
            LoadingScreen(
                navController = navController,
                mainActivity = mainActivity,
                onSplashLoaded = {
                    mainActivity.keepSplashOpened = false
                }
            )
        }
        composable(route = WIZARD_ROUTE) {
            WizardScreen(navController = navController, mainActivity = mainActivity)
        }
        loginNavGraph(navController, mainActivity)
        composable(route = MAIN_ROUTE) {
            MainScreen(navController = rememberNavController(), mainActivity = mainActivity)
        }
    }
}