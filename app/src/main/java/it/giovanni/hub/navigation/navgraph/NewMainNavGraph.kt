package it.giovanni.hub.navigation.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.routes.MainRoutes
import it.giovanni.hub.presentation.screen.main.MainScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel

@ExperimentalAnimationApi
fun NavGraphBuilder.mainScreenNavGraph(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    onThemeUpdated: () -> Unit,
    onColorUpdated: () -> Unit,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    counterService: CounterService
) {
    navigation(
        route = Graph.MAIN_ROUTE,
        startDestination = MainRoutes.Main.route
    ) {
        composable(
            route = MainRoutes.Main.route
        ) {
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