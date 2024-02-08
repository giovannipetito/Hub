package it.giovanni.hub.navigation.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.giovanni.hub.navigation.Graph.WIZARD_ROUTE
import it.giovanni.hub.navigation.Graph.ROOT_ROUTE
import it.giovanni.hub.navigation.Graph.LOADING_ROUTE
import it.giovanni.hub.presentation.screen.main.LoadingScreen
import it.giovanni.hub.presentation.screen.main.WizardScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

@ExperimentalAnimationApi
@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val personViewModel: PersonViewModel = viewModel() // SharedViewModel

    // Root Navigation Graph
    NavHost(
        navController = navController,
        route = ROOT_ROUTE,
        startDestination = LOADING_ROUTE
    ) {
        composable(route = LOADING_ROUTE) {
            LoadingScreen(
                navController = navController,
                onSplashLoaded = {
                    mainViewModel.setSplashOpened(state = false)
                }
            )
        }

        composable(route = WIZARD_ROUTE) {
            WizardScreen(navController = navController)
        }

        loginNavGraph(navController = navController, mainViewModel = mainViewModel)

        // Nested Navigation Graphs
        homeNavGraph(navController = navController, mainViewModel = mainViewModel)

        profileNavGraph(
            navController = navController,
            personViewModel = personViewModel
        )

        settingsNavGraph(navController = navController)
    }
}