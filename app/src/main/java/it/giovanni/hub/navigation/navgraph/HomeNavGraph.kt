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
import it.giovanni.hub.Graph.HOME_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.navigation.set.MainSet
import it.giovanni.hub.presentation.screen.detail.AnimatedShimmerScreen
import it.giovanni.hub.presentation.screen.detail.Detail1Screen
import it.giovanni.hub.presentation.screen.detail.Detail2Screen
import it.giovanni.hub.presentation.screen.detail.PagingScreen
import it.giovanni.hub.presentation.screen.detail.ShuffledScreen
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.screen.detail.TextFieldsScreen
import it.giovanni.hub.presentation.screen.detail.UIScreen
import it.giovanni.hub.presentation.screen.detail.UsersRxScreen
import it.giovanni.hub.presentation.screen.detail.UsersScreen

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    mainActivity: MainActivity
) {
    navigation(
        route = HOME_ROUTE,
        startDestination = MainSet.Home.route
    ) {
        composable(
            route = MainSet.Home.route
        ) {
            HomeScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = MainSet.Detail1.route,
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
            route = MainSet.Detail2.route,
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
            route = MainSet.TextFields.route
        ) {
            TextFieldsScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = MainSet.Users.route
        ) {
            UsersScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = MainSet.UsersRx.route
        ) {
            UsersRxScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = MainSet.UI.route
        ) {
            UIScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = MainSet.AnimatedShimmer.route
        ) {
            AnimatedShimmerScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = MainSet.Shuffled.route
        ) {
            ShuffledScreen(navController = navController, mainActivity = mainActivity)
        }

        composable(
            route = MainSet.Paging.route
        ) {
            PagingScreen(navController = navController, mainActivity = mainActivity)
        }

        // authNavGraph(navController, mainActivity) // Sta bene sia in MainNavGraph che HomeNavGraph.
    }
}

/*
fun Card(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
)
 */

/*
fun Card(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit
)
 */