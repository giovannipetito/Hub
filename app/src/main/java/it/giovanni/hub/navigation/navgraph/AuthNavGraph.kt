package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Auth
import it.giovanni.hub.navigation.util.routes.AuthRoutes
import it.giovanni.hub.presentation.screen.main.AuthScreen
import it.giovanni.hub.presentation.screen.detail.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation<Auth>(
        startDestination = AuthRoutes.Auth
    ) {
        composable<AuthRoutes.Auth> {
            AuthScreen(navController = navController)
        }

        composable<AuthRoutes.SignUp> {
            SignUpScreen(navController = navController)
        }
    }
}