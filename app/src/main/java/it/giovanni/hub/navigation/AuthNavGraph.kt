package it.giovanni.hub.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.screens.LoginScreen
import it.giovanni.hub.screens.SignUpScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Login.route,
        route = AUTH_ROUTE
    ) {
        composable(
            route = Screen.Login.route
        ) {
            LoginScreen(navController)
        }

        composable(
            route = Screen.SignUp.route
        ) {
            SignUpScreen(navController)
        }
    }
}