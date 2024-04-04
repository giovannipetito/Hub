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
import it.giovanni.hub.presentation.screen.detail.Detail1Screen
import it.giovanni.hub.presentation.screen.detail.Detail2Screen
import it.giovanni.hub.presentation.screen.detail.Detail3Screen
import it.giovanni.hub.presentation.screen.detail.Detail4Screen
import it.giovanni.hub.presentation.screen.detail.MultiplePermissionsScreen
import it.giovanni.hub.presentation.screen.detail.PagingScreen
import it.giovanni.hub.presentation.screen.detail.PermissionScreen
import it.giovanni.hub.presentation.screen.detail.PersonStateScreen
import it.giovanni.hub.presentation.screen.detail.UsersRxJavaScreen
import it.giovanni.hub.presentation.screen.detail.UsersCoroutinesScreen
import it.giovanni.hub.presentation.screen.detail.WebViewScreen
import it.giovanni.hub.presentation.screen.main.ProfileScreen
import it.giovanni.hub.presentation.viewmodel.PersonViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import it.giovanni.hub.navigation.util.routes.MainRoutes
import it.giovanni.hub.navigation.util.routes.ProfileRoutes
import it.giovanni.hub.presentation.screen.detail.ContactsScreen
import it.giovanni.hub.presentation.screen.detail.CounterServiceScreen
import it.giovanni.hub.presentation.screen.detail.ErrorHandlingScreen
import it.giovanni.hub.presentation.screen.detail.HeaderScreen
import it.giovanni.hub.presentation.screen.detail.PullToRefreshScreen
import it.giovanni.hub.presentation.screen.detail.StickyHeaderScreen
import it.giovanni.hub.presentation.screen.detail.SwipeActionsScreen
import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY1
import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY2

@ExperimentalAnimationApi
fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    personViewModel: PersonViewModel
) {
    navigation(
        route = Graph.PROFILE_ROUTE,
        startDestination = MainRoutes.Profile.route
    ) {
        composable(
            route = MainRoutes.Profile.route
        ) {
            ProfileScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.Detail1.route,
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
            Detail1Screen(navController = navController)
        }

        composable(
            route = ProfileRoutes.Detail2.route,
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
            Detail2Screen(navController = navController)
        }

        composable(
            route = ProfileRoutes.Detail3.route
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
            route = ProfileRoutes.Detail4.route
        ) {
            Detail4Screen(navController = navController, personViewModel)
        }

        composable(
            route = ProfileRoutes.PersonState.route
        ) {
            PersonStateScreen(navController = navController)
        }

        authNavGraph(navController = navController)

        composable(
            route = ProfileRoutes.Contacts.route
        ) {
            ContactsScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.Header.route
        ) {
            HeaderScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.StickyHeader.route
        ) {
            StickyHeaderScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.SwipeActions.route
        ) {
            SwipeActionsScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.UsersCoroutines.route
        ) {
            UsersCoroutinesScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.UsersRxJava.route
        ) {
            UsersRxJavaScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.PullToRefresh.route
        ) {
            PullToRefreshScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.Paging.route
        ) {
            PagingScreen(navController = navController)
        }

        @OptIn(ExperimentalPermissionsApi::class)
        composable(
            route = ProfileRoutes.SinglePermission.route
        ) {
            PermissionScreen(navController = navController)
        }

        @OptIn(ExperimentalPermissionsApi::class)
        composable(
            route = ProfileRoutes.MultiplePermissions.route
        ) {
            MultiplePermissionsScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.WebView.route
        ) {
            WebViewScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.CounterService.route
        ) {
            CounterServiceScreen(navController = navController)
        }

        composable(
            route = ProfileRoutes.ErrorHandling.route
        ) {
            ErrorHandlingScreen(navController = navController)
        }
    }
}