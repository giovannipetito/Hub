package it.giovanni.hub.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import it.giovanni.hub.Constants.HOME_ROUTE
import it.giovanni.hub.Constants.ROOT_ROUTE

@Composable
fun SetupNavGraph(navController: NavHostController) {

    // Root Navigation Graph
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        route = ROOT_ROUTE
    ) {
        // Nested Navigation Graphs
        homeNavGraph(navController)
        authNavGraph(navController)
    }
}