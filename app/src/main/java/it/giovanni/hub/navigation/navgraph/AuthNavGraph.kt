package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.Graph.AUTH_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.navigation.screen.Screen
import it.giovanni.hub.screens.main.LoginScreen
import it.giovanni.hub.screens.detail.SignUpScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    mainActivity: MainActivity
) {
    navigation(
        route = AUTH_ROUTE,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = Screen.SignUp.route
        ) {
            SignUpScreen(navController = navController, mainActivity = mainActivity)
        }
    }
}