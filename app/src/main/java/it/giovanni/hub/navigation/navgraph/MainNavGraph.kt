package it.giovanni.hub.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.giovanni.hub.Graph.BOTTOM_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.screens.detail.ProfileScreen
import it.giovanni.hub.screens.detail.SettingsScreen
import it.giovanni.hub.navigation.set.BottomBarSet
import it.giovanni.hub.screens.main.HomeScreen

@Composable
fun MainNavGraph(navController: NavHostController, mainActivity: MainActivity) {

    // Main Navigation Graph
    NavHost(
        navController = navController,
        route = BOTTOM_ROUTE,
        startDestination = BottomBarSet.Home.route
    ) {
        composable(route = BottomBarSet.Home.route) {
            HomeScreen(navController = navController, mainActivity = mainActivity)
        }
        composable(route = BottomBarSet.Profile.route) {
            ProfileScreen(navController = navController, mainActivity = mainActivity)
        }
        composable(route = BottomBarSet.Settings.route) {
            SettingsScreen(navController = navController, mainActivity = mainActivity)
        }
        // Nested Navigation Graphs
        homeNavGraph(navController, mainActivity)
        authNavGraph(navController, mainActivity) // Sta bene sia in MainNavGraph che HomeNavGraph.
    }
}