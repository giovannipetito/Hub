package it.giovanni.hub.navigation.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.navigation.Graph.BOTTOM_ROUTE
import it.giovanni.hub.navigation.util.entries.BottomAppBarEntries
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.screen.main.ProfileScreen
import it.giovanni.hub.presentation.screen.main.SettingsScreen
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
        startDestination = BottomAppBarEntries.Home.route,
    ) {
        composable(route = BottomAppBarEntries.Home.route) {
            HomeScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable(route = BottomAppBarEntries.Profile.route) {
            ProfileScreen(navController = navController)
        }

        composable(route = BottomAppBarEntries.Settings.route) {
            SettingsScreen(navController = navController)
        }

        // Nested Navigation Graphs
        homeNavGraph(navController = navController, mainViewModel = mainViewModel)

        profileNavGraph(
            navController = navController,
            personViewModel = personViewModel,
            counterService = counterService
        )

        settingsNavGraph(navController = navController)

        // Necessario per il logout/sign-out.
        loginNavGraph(navController = navController, mainViewModel = mainViewModel)
    }
}