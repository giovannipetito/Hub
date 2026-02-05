package it.giovanni.hub.navigation.navgraph

import android.util.Log
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.navigation.routes.Profile
import it.giovanni.hub.presentation.screen.detail.Detail1Screen
import it.giovanni.hub.presentation.screen.detail.Detail2Screen
import it.giovanni.hub.presentation.screen.detail.MultiplePermissionsScreen
import it.giovanni.hub.presentation.screen.detail.PagingScreen
import it.giovanni.hub.presentation.screen.detail.PermissionScreen
import it.giovanni.hub.presentation.screen.detail.PersonStateScreen
import it.giovanni.hub.presentation.screen.detail.UsersRxJavaScreen
import it.giovanni.hub.presentation.screen.detail.UsersCoroutinesScreen
import it.giovanni.hub.presentation.screen.detail.WebViewScreen
import it.giovanni.hub.presentation.screen.main.ProfileScreen
import it.giovanni.hub.presentation.viewmodel.PersonViewModel
import it.giovanni.hub.navigation.routes.BottomBarRoutes
import it.giovanni.hub.navigation.routes.ProfileRoutes
import it.giovanni.hub.presentation.screen.detail.BirthdayScreen
import it.giovanni.hub.presentation.screen.detail.ContactsScreen
import it.giovanni.hub.presentation.screen.detail.CounterServiceScreen
import it.giovanni.hub.presentation.screen.detail.ErrorHandlingScreen
import it.giovanni.hub.presentation.screen.detail.HeaderScreen
import it.giovanni.hub.presentation.screen.detail.PullToRefreshScreen
import it.giovanni.hub.presentation.screen.detail.RealtimeScreen
import it.giovanni.hub.presentation.screen.detail.RoomCoroutinesScreen
import it.giovanni.hub.presentation.screen.detail.RoomRxJavaScreen
import it.giovanni.hub.presentation.screen.detail.StickyHeaderScreen
import it.giovanni.hub.presentation.screen.detail.SwipeActionsScreen
import it.giovanni.hub.presentation.screen.detail.GeminiScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.presentation.viewmodel.comfyui.ComfyUIViewModel

fun NavGraphBuilder.profileNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    personViewModel: PersonViewModel,
    comfyUIViewModel: ComfyUIViewModel
) {
    navigation(
        route = Profile.toString(),
        startDestination = BottomBarRoutes.Profile.route
    ) {
        composable(
            route = BottomBarRoutes.Profile.route
        ) {
            ProfileScreen(navController = navController)
        }

        composable<ProfileRoutes.Detail1> {
            /*
            Il log viene stampato due volte, per evitare questo comportamento usiamo LaunchedEffect
            passando il NavBackStackEntry come chiave, così solo quando il NavBackStackEntry cambia
            stampiamo a video il log.
            */
            LaunchedEffect(key1 = it) {
                val person: Person? = navController.previousBackStackEntry?.savedStateHandle?.get<Person>(key = "person")
                Log.i("[Person]", person?.firstName + " " + person?.lastName)
            }
            Detail1Screen(navController = navController, personViewModel = personViewModel)
        }

        composable<ProfileRoutes.Detail2> {
            Detail2Screen(navController = navController, personViewModel)
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
            CounterServiceScreen(navController = navController, mainViewModel = mainViewModel)
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

        composable<ProfileRoutes.Gemini> {
            GeminiScreen(navController = navController)
        }

        comfyUINavGraph(navController = navController, comfyUIViewModel = comfyUIViewModel)

        composable<ProfileRoutes.Birthday> {
            BirthdayScreen(navController = navController)
        }
    }
}