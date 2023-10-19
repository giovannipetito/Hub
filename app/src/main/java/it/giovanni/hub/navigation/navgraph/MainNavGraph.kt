package it.giovanni.hub.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.giovanni.hub.Graph.BOTTOM_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.screens.detail.ProfileScreen
import it.giovanni.hub.screens.detail.SettingsScreen
import it.giovanni.hub.screens.main.BottomBarScreen
import it.giovanni.hub.screens.main.HomeScreen

@Composable
fun SetupMainNavGraph(navController: NavHostController, mainActivity: MainActivity) {

    // Main Navigation Graph
    NavHost(
        navController = navController,
        route = BOTTOM_ROUTE,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController = navController, mainActivity = mainActivity)
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController = navController, mainActivity = mainActivity)
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(navController = navController, mainActivity = mainActivity)
        }
        // Nested Navigation Graphs
        homeNavGraph(navController, mainActivity)
        authNavGraph(navController, mainActivity) // Sta bene sia in MainNavGraph che HomeNavGraph.
    }
}