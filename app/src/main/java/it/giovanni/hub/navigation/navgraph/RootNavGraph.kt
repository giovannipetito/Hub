package it.giovanni.hub.navigation.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.navigation.Graph.MAIN_ROUTE
import it.giovanni.hub.navigation.Graph.WIZARD_ROUTE
import it.giovanni.hub.navigation.Graph.ROOT_ROUTE
import it.giovanni.hub.navigation.Graph.LOADING_ROUTE
import it.giovanni.hub.presentation.screen.main.MainScreen
import it.giovanni.hub.presentation.screen.main.LoadingScreen
import it.giovanni.hub.presentation.screen.main.WizardScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel

@ExperimentalAnimationApi
@Composable
fun RootNavGraph(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    onThemeUpdated: () -> Unit,
    onColorUpdated: () -> Unit,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    counterService: CounterService
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
                onSplashLoaded = {
                    mainViewModel.setSplashOpened(state = false)
                }
            )
        }
        composable(route = WIZARD_ROUTE) {
            WizardScreen(navController = navController)
        }
        loginNavGraph(navController = navController, mainViewModel = mainViewModel)
        composable(route = MAIN_ROUTE) {
            MainScreen(
                darkTheme = darkTheme,
                dynamicColor = dynamicColor,
                onThemeUpdated = onThemeUpdated,
                onColorUpdated = onColorUpdated,
                navController = rememberNavController(),
                mainViewModel = mainViewModel,
                counterService = counterService
            )
        }
    }
}