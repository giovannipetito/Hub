package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Graph.AUTH_ROUTE
import it.giovanni.hub.navigation.util.routes.AuthRoutes
import it.giovanni.hub.presentation.screen.main.AuthScreen
import it.giovanni.hub.presentation.screen.detail.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = AUTH_ROUTE,
        startDestination = AuthRoutes.Auth.route
    ) {
        composable(
            route = AuthRoutes.Auth.route
        ) {
            AuthScreen(navController = navController)
        }

        composable<AuthRoutes.SignUp> {
            SignUpScreen(navController = navController)
        }
    }
}