package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Home
import it.giovanni.hub.navigation.util.routes.MainRoutes
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    navigation(
        route = Home.toString(),
        startDestination = MainRoutes.Home.route
    ) {
        composable(
            route = MainRoutes.Home.route
        ) {
            HomeScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
    }
}