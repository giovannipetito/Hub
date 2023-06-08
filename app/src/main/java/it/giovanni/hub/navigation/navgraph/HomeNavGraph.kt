package it.giovanni.hub.navigation.navgraph

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import it.giovanni.hub.Constants.DETAIL_ARG_KEY1
import it.giovanni.hub.Constants.DETAIL_ARG_KEY2
import it.giovanni.hub.Constants.HOME_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.navigation.Screen
import it.giovanni.hub.screens.details.Detail1Screen
import it.giovanni.hub.screens.details.Detail2Screen
import it.giovanni.hub.screens.HomeScreen
import it.giovanni.hub.screens.details.TextFieldsScreen
import it.giovanni.hub.screens.details.UsersRxScreen
import it.giovanni.hub.screens.details.UsersScreen

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    mainActivity: MainActivity
) {
    navigation(
        startDestination = Screen.Home.route,
        route = HOME_ROUTE
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController = navController, mainActivity = MainActivity())
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
            Detail1Screen(navController = navController, mainActivity = mainActivity)
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
            Detail2Screen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = Screen.TextFields.route
        ) {
            TextFieldsScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = Screen.Users.route
        ) {
            UsersScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = Screen.UsersRx.route
        ) {
            UsersRxScreen(navController = navController, mainActivity = mainActivity)
        }
    }
}