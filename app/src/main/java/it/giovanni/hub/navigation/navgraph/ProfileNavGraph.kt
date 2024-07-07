package it.giovanni.hub.navigation.navgraph

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
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
import it.giovanni.hub.presentation.screen.detail.RealmScreen
import it.giovanni.hub.presentation.screen.detail.RealtimeScreen
import it.giovanni.hub.presentation.screen.detail.RoomCoroutinesScreen
import it.giovanni.hub.presentation.screen.detail.RoomRxJavaScreen
import it.giovanni.hub.presentation.screen.detail.StickyHeaderScreen
import it.giovanni.hub.presentation.screen.detail.SwipeActionsScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel

@ExperimentalAnimationApi
fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
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

        composable<ProfileRoutes.Detail1> {
            val detail1 = it.toRoute<ProfileRoutes.Detail1>()
            Detail1Screen(navController = navController, id = detail1.id, name = detail1.name)
        }

        composable<ProfileRoutes.Detail2> {
            val detail2 = it.toRoute<ProfileRoutes.Detail2>()
            Detail2Screen(navController = navController, id = detail2.id, name = detail2.name)
        }

        composable<ProfileRoutes.Detail3> {
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

        composable<ProfileRoutes.Detail4> {
            Detail4Screen(navController = navController, personViewModel)
        }

        composable<ProfileRoutes.PersonState> {
            PersonStateScreen(navController = navController)
        }

        authNavGraph(navController = navController)

        composable<ProfileRoutes.Contacts> {
            ContactsScreen(navController = navController)
        }

        composable<ProfileRoutes.Header> {
            HeaderScreen(navController = navController)
        }

        composable<ProfileRoutes.StickyHeader> {
            StickyHeaderScreen(navController = navController)
        }

        composable<ProfileRoutes.SwipeActions> {
            SwipeActionsScreen(navController = navController)
        }

        composable<ProfileRoutes.UsersCoroutines> {
            UsersCoroutinesScreen(navController = navController)
        }

        composable<ProfileRoutes.UsersRxJava> {
            UsersRxJavaScreen(navController = navController)
        }

        composable<ProfileRoutes.PullToRefresh> {
            PullToRefreshScreen(navController = navController)
        }

        composable<ProfileRoutes.Paging> {
            PagingScreen(navController = navController)
        }

        @OptIn(ExperimentalPermissionsApi::class)
        composable<ProfileRoutes.SinglePermission> {
            PermissionScreen(navController = navController)
        }

        @OptIn(ExperimentalPermissionsApi::class)
        composable<ProfileRoutes.MultiplePermissions> {
            MultiplePermissionsScreen(navController = navController)
        }

        composable<ProfileRoutes.WebView> {
            WebViewScreen(navController = navController)
        }

        composable<ProfileRoutes.CounterService> {
            CounterServiceScreen(navController = navController)
        }

        composable<ProfileRoutes.ErrorHandling> {
            ErrorHandlingScreen(navController = navController)
        }
        composable<ProfileRoutes.RoomCoroutines> {
            RoomCoroutinesScreen(navController = navController)
        }
        composable<ProfileRoutes.RoomRxJava> {
            RoomRxJavaScreen(navController = navController)
        }
        composable<ProfileRoutes.Realtime> {
            RealtimeScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable<ProfileRoutes.Realm> {
            RealmScreen(navController = navController)
        }
    }
}