package it.giovanni.hub.navigation.navgraph

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY1
import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY2
import it.giovanni.hub.navigation.Graph.HOME_ROUTE
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.presentation.screen.detail.ShimmerScreen
import it.giovanni.hub.presentation.screen.detail.Detail1Screen
import it.giovanni.hub.presentation.screen.detail.Detail2Screen
import it.giovanni.hub.presentation.screen.detail.Detail3Screen
import it.giovanni.hub.presentation.screen.detail.Detail4Screen
import it.giovanni.hub.presentation.screen.detail.PagingScreen
import it.giovanni.hub.presentation.screen.detail.HyperlinkScreen
import it.giovanni.hub.presentation.screen.detail.MultiplePermissionsScreen
import it.giovanni.hub.presentation.screen.detail.PermissionScreen
import it.giovanni.hub.presentation.screen.detail.ShuffledScreen
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.screen.detail.TextFieldsScreen
import it.giovanni.hub.presentation.screen.detail.UIScreen
import it.giovanni.hub.presentation.screen.detail.UsersRxScreen
import it.giovanni.hub.presentation.screen.detail.UsersScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    personViewModel: PersonViewModel
) {
    navigation(
        route = HOME_ROUTE,
        startDestination = MainSet.Home.route
    ) {
        composable(
            route = MainSet.Home.route
        ) {
            HomeScreen(navController = navController, mainViewModel = mainViewModel)
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
            Detail1Screen(navController = navController)
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
            route = MainSet.TextFields.route
        ) {
            TextFieldsScreen(navController = navController)
        }

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
            route = MainSet.UI.route
        ) {
            UIScreen(navController = navController)
        }

        composable(
            route = MainSet.Shimmer.route
        ) {
            ShimmerScreen(navController = navController)
        }

        composable(
            route = MainSet.Shuffled.route
        ) {
            ShuffledScreen(navController = navController)
        }

        composable(
            route = MainSet.Paging.route
        ) {
            PagingScreen(navController = navController)
        }

        composable(
            route = MainSet.Hyperlink.route
        ) {
            HyperlinkScreen(navController = navController)
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

        authNavGraph(navController)
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