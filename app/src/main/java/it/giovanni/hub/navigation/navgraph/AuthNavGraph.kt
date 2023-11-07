package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.Graph.AUTH_ROUTE
import it.giovanni.hub.navigation.util.set.AuthSet
import it.giovanni.hub.presentation.screen.main.AuthScreen
import it.giovanni.hub.presentation.screen.detail.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = AUTH_ROUTE,
        startDestination = AuthSet.Auth.route
    ) {
        composable(
            route = AuthSet.Auth.route
        ) {
            AuthScreen(navController = navController)
        }

        composable(
            route = AuthSet.SignUp.route
        ) {
            SignUpScreen(navController = navController)
        }
    }
}