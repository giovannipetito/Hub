package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.LoginSet
import it.giovanni.hub.presentation.screen.detail.InfoScreen
import it.giovanni.hub.presentation.screen.main.LoginScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    navigation(
        route = Graph.LOGIN_ROUTE,
        startDestination = LoginSet.Login.route
    ) {
        composable(
            route = LoginSet.Login.route
        ) {
            LoginScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable(
            route = LoginSet.Info.route
        ) {
            InfoScreen(navController = navController)
        }
    }
}