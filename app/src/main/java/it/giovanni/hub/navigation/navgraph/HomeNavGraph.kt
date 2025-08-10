package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.routes.Home
import it.giovanni.hub.navigation.routes.BottomBarRoutes
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    navigation(
        route = Home.toString(),
        startDestination = BottomBarRoutes.Home.route
    ) {
        composable(
            route = BottomBarRoutes.Home.route
        ) {
            HomeScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
    }
}