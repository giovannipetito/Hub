package it.giovanni.hub.navigation.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.navigation.Graph.BOTTOM_ROUTE
import it.giovanni.hub.presentation.screen.main.ProfileScreen
import it.giovanni.hub.presentation.screen.main.SettingsScreen
import it.giovanni.hub.navigation.util.set.BottomAppBarSet
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

@ExperimentalAnimationApi
@Composable
fun MainNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    counterService: CounterService
) {
    val personViewModel: PersonViewModel = viewModel() // SharedViewModel

    // Main Navigation Graph
    NavHost(
        navController = navController,
        route = BOTTOM_ROUTE,
        startDestination = BottomAppBarSet.Home.route
    ) {
        composable(route = BottomAppBarSet.Home.route) {
            HomeScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable(route = BottomAppBarSet.Profile.route) {
            ProfileScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable(route = BottomAppBarSet.Settings.route) {
            SettingsScreen(navController = navController, mainViewModel = mainViewModel)
        }

        // Nested Navigation Graphs
        homeNavGraph(navController = navController, mainViewModel = mainViewModel)

        profileNavGraph(
            navController = navController,
            mainViewModel = mainViewModel,
            personViewModel = personViewModel,
            counterService = counterService
        )
        settingsNavGraph(navController = navController, mainViewModel = mainViewModel)

        loginNavGraph(navController = navController, mainViewModel = mainViewModel) // Necessario per poter fare Logout.
    }
}