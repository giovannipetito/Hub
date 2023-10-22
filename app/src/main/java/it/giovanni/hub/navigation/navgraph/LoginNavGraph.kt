package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.Graph
import it.giovanni.hub.MainActivity
import it.giovanni.hub.navigation.set.LoginSet
import it.giovanni.hub.presentation.screen.detail.InfoScreen
import it.giovanni.hub.presentation.screen.main.LoginScreen

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
    mainActivity: MainActivity
) {
    navigation(
        route = Graph.LOGIN_ROUTE,
        startDestination = LoginSet.Login.route
    ) {
        composable(
            route = LoginSet.Login.route
        ) {
            LoginScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = LoginSet.Info.route
        ) {
            InfoScreen(navController = navController, mainActivity = mainActivity)
        }
    }
}