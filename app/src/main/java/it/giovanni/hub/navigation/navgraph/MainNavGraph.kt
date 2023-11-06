package it.giovanni.hub.navigation.navgraph

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.giovanni.hub.Graph.BOTTOM_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.presentation.screen.main.ProfileScreen
import it.giovanni.hub.presentation.screen.main.SettingsScreen
import it.giovanni.hub.navigation.util.set.BottomBarSet
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

@Composable
fun MainNavGraph(navController: NavHostController, mainActivity: MainActivity) {

    val personViewModel: PersonViewModel = viewModel() // SharedViewModel

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
        homeNavGraph(navController, mainActivity, personViewModel)
        // profileNavGraph(navController, mainActivity)
        // settingsNavGraph(navController, mainActivity)

        loginNavGraph(navController, mainActivity) // Necessario per poter fare Logout.
    }
}