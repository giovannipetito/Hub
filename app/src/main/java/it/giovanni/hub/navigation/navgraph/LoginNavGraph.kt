package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.Graph
import it.giovanni.hub.MainActivity
import it.giovanni.hub.navigation.screen.Screen
import it.giovanni.hub.screens.detail.SignUpScreen
import it.giovanni.hub.screens.main.AuthScreen

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
    mainActivity: MainActivity
) {
    navigation(
        route = Graph.LOGIN_ROUTE,
        startDestination = Screen.Auth.route
    ) {
        composable(
            route = Screen.Auth.route
        ) {
            AuthScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = Screen.SignUp.route
        ) {
            SignUpScreen(navController = navController, mainActivity = mainActivity)
        }
    }
}