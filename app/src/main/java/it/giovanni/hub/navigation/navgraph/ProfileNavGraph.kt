package it.giovanni.hub.navigation.navgraph

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.presentation.screen.detail.Detail1Screen
import it.giovanni.hub.presentation.screen.detail.Detail2Screen
import it.giovanni.hub.presentation.screen.detail.Detail3Screen
import it.giovanni.hub.presentation.screen.detail.Detail4Screen
import it.giovanni.hub.presentation.screen.detail.HyperlinkScreen
import it.giovanni.hub.presentation.screen.detail.MultiplePermissionsScreen
import it.giovanni.hub.presentation.screen.detail.PagingScreen
import it.giovanni.hub.presentation.screen.detail.PermissionScreen
import it.giovanni.hub.presentation.screen.detail.PersonStateScreen
import it.giovanni.hub.presentation.screen.detail.UsersRxScreen
import it.giovanni.hub.presentation.screen.detail.UsersScreen
import it.giovanni.hub.presentation.screen.detail.WebViewScreen
import it.giovanni.hub.presentation.screen.main.ProfileScreen
import it.giovanni.hub.presentation.viewmodel.PersonViewModel
import it.giovanni.hub.utils.Constants
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.presentation.screen.detail.CounterServiceScreen
import it.giovanni.hub.presentation.screen.detail.HubColorsScreen

@ExperimentalAnimationApi
fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    personViewModel: PersonViewModel,
    counterService: CounterService
) {
    navigation(
        route = Graph.PROFILE_ROUTE,
        startDestination = MainSet.Profile.route
    ) {
        composable(
            route = MainSet.Profile.route
        ) {
            ProfileScreen(navController = navController)
        }

        composable(
            route = MainSet.Detail1.route,
            arguments =
            listOf(
                navArgument(Constants.DETAIL_ARG_KEY1) {
                    type = NavType.IntType
                },
                navArgument(Constants.DETAIL_ARG_KEY2) {
                    type = NavType.StringType
                }
            )
        ) {
            Log.d("[Args]", "Required id: " + it.arguments?.getInt(Constants.DETAIL_ARG_KEY1).toString())
            Log.d("[Args]", "Required name: " + it.arguments?.getString(Constants.DETAIL_ARG_KEY2).toString())
            Detail1Screen(navController = navController)
        }

        composable(
            route = MainSet.Detail2.route,
            arguments =
            listOf(
                navArgument(Constants.DETAIL_ARG_KEY1) {
                    type = NavType.IntType
                    defaultValue = 0 // Oppure: nullable = true --> Se non passi alcun argomento, setta automaticamente l'argomento a null
                },
                navArgument(Constants.DETAIL_ARG_KEY2) {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            Log.d("[Args]", "Optional id: " + it.arguments?.getInt(Constants.DETAIL_ARG_KEY1).toString())
            Log.d("[Args]", "Optional name: " + it.arguments?.getString(Constants.DETAIL_ARG_KEY2).toString())
            Detail2Screen(navController = navController)
        }

        composable(
            route = MainSet.Detail3.route
        ) {
            /*
            Il log viene stampato due volte, per evitare questo comportamento usiamo LaunchedEffect
            passando il NavBackStackEntry come chiave, così solo quando il NavBackStackEntry cambia
            stampiamo a video il log.
            */
            LaunchedEffect(key1 = it) {
                val person: Person? = navController.previousBackStackEntry?.savedStateHandle?.get<Person>(key = "person")
                Log.i("[Person]", person?.firstName + " " + person?.lastName)
            }
            Detail3Screen(navController = navController, personViewModel = personViewModel)
        }

        composable(
            route = MainSet.Detail4.route
        ) {
            Detail4Screen(navController = navController, personViewModel)
        }

        composable(
            route = MainSet.PersonState.route
        ) {
            PersonStateScreen(navController = navController)
        }

        authNavGraph(navController = navController)

        composable(
            route = MainSet.Users.route
        ) {
            UsersScreen(navController = navController)
        }

        composable(
            route = MainSet.UsersRx.route
        ) {
            UsersRxScreen(navController = navController)
        }

        composable(
            route = MainSet.Paging.route
        ) {
            PagingScreen(navController = navController)
        }

        @OptIn(ExperimentalPermissionsApi::class)
        composable(
            route = MainSet.SinglePermission.route
        ) {
            PermissionScreen(navController = navController)
        }

        @OptIn(ExperimentalPermissionsApi::class)
        composable(
            route = MainSet.MultiplePermissions.route
        ) {
            MultiplePermissionsScreen(navController = navController)
        }

        composable(
            route = MainSet.Hyperlink.route
        ) {
            HyperlinkScreen(navController = navController)
        }

        composable(
            route = MainSet.WebView.route
        ) {
            WebViewScreen(navController = navController)
        }

        composable(
            route = MainSet.CounterService.route
        ) {
            CounterServiceScreen(navController = navController, counterService = counterService)
        }

        composable(
            route = MainSet.Colors.route
        ) {
            HubColorsScreen(navController = navController)
        }
    }
}