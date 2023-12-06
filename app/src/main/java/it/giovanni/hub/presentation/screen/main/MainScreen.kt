package it.giovanni.hub.presentation.screen.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.navigation.navgraph.MainNavGraph
import it.giovanni.hub.navigation.util.set.BottomAppBarSet
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.HubModalNavigationDrawer
import it.giovanni.hub.utils.Globals.getCurrentRoute1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    counterService: CounterService
) {
    val currentRoute = getCurrentRoute1(navController = navController)

    // List of routes where the drawer should be visible.
    val bottomAppBarRoutes = listOf(
        BottomAppBarSet.Home.route,
        BottomAppBarSet.Profile.route,
        BottomAppBarSet.Settings.route
    )

    // Show the drawer only on specified routes.
    if (currentRoute in bottomAppBarRoutes) {
        HubModalNavigationDrawer(navController = navController) {
            MainNavGraph(
                navController = navController,
                mainViewModel = mainViewModel,
                counterService = counterService
            )
        }
    } else {
        MainNavGraph(
            navController = navController,
            mainViewModel = mainViewModel,
            counterService = counterService
        )
    }

    // Implementation without NavigationDrawer.
    /*
    Scaffold(
        bottomBar = {
            HubBottomAppBar(navController = navController)
        }
    ) {
        MainNavGraph(navController = navController, mainViewModel = mainViewModel, counterService = counterService)
    }
    */
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController(), mainViewModel = hiltViewModel(), counterService = CounterService())
}