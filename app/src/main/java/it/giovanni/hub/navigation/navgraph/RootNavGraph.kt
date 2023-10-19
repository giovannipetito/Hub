package it.giovanni.hub.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.Graph.HOME_ROUTE
import it.giovanni.hub.Graph.MAIN_ROUTE
import it.giovanni.hub.Graph.ROOT_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.screens.main.MainScreen

@Composable
fun RootNavigationGraph(navController: NavHostController, mainActivity: MainActivity) {

    // Root Navigation Graph
    NavHost(
        navController = navController,
        route = ROOT_ROUTE,
        startDestination = MAIN_ROUTE
    ) {
        // loginNavGraph(navController, mainActivity)
        composable(route = MAIN_ROUTE) {
            MainScreen(navController = rememberNavController(), mainActivity = mainActivity)
        }
    }
}