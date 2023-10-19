package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.Graph.AUTH_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.navigation.set.AuthSet
import it.giovanni.hub.screens.main.AuthScreen
import it.giovanni.hub.screens.detail.SignUpScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    mainActivity: MainActivity
) {
    navigation(
        route = AUTH_ROUTE,
        startDestination = AuthSet.Auth.route
    ) {
        composable(
            route = AuthSet.Auth.route
        ) {
            AuthScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = AuthSet.SignUp.route
        ) {
            SignUpScreen(navController = navController, mainActivity = mainActivity)
        }
    }
}