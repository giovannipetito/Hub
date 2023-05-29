package it.giovanni.hub.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import it.giovanni.hub.screens.Detail1Screen
import it.giovanni.hub.screens.Detail2Screen
import it.giovanni.hub.screens.HomeScreen
import it.giovanni.hub.screens.LoginScreen
import it.giovanni.hub.screens.SignUpScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {

    // Root Navigation Graph
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        route = ROOT_ROUTE
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController)
        }

        composable(
            route = Screen.Detail1.route,
            arguments =
            listOf(
                navArgument(DETAIL_ARG_KEY1) {
                    type = NavType.IntType
                },
                navArgument(DETAIL_ARG_KEY2) {
                    type = NavType.StringType
                }
            )
        ) {
            Log.d("[Args]", "Required id: " + it.arguments?.getInt(DETAIL_ARG_KEY1).toString())
            Log.d("[Args]", "Required name: " + it.arguments?.getString(DETAIL_ARG_KEY2).toString())
            Detail1Screen(navController)
        }

        composable(
            route = Screen.Detail2.route,
            arguments =
            listOf(
                navArgument(DETAIL_ARG_KEY1) {
                    type = NavType.IntType
                    defaultValue = 0 // Oppure: nullable = true --> Se non passi alcun argomento, setta automaticamente l'argomento a null
                },
                navArgument(DETAIL_ARG_KEY2) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            Log.d("[Args]", "Optional id: " + it.arguments?.getInt(DETAIL_ARG_KEY1).toString())
            Log.d("[Args]", "Optional name: " + it.arguments?.getString(DETAIL_ARG_KEY2).toString())
            Detail2Screen(navController)
        }

        // Nested Navigation Graph
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
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SetupNavGraph(
        navController = rememberNavController()
    )
}