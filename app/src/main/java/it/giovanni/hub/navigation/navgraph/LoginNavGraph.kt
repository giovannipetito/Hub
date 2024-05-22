package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Login
import it.giovanni.hub.navigation.util.routes.LoginRoutes
import it.giovanni.hub.presentation.screen.detail.InfoScreen
import it.giovanni.hub.presentation.screen.main.LoginScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    navigation<Login>(
        startDestination = LoginRoutes.Login
    ) {
        composable<LoginRoutes.Login> {
            LoginScreen(navController = navController, mainViewModel = mainViewModel)
        }

        composable<LoginRoutes.Info> {
            InfoScreen(navController = navController)
        }
    }
}