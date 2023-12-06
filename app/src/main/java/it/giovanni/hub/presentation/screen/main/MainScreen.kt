package it.giovanni.hub.presentation.screen.main

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.navigation.navgraph.MainNavGraph
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.HubModalNavigationDrawer
import it.giovanni.hub.utils.Globals.bottomAppBarRoutes
import it.giovanni.hub.utils.Globals.getCurrentRoute1

@ExperimentalAnimationApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    counterService: CounterService
) {
    val currentRoute = getCurrentRoute1(navController = navController)

    // Show the drawer only on main routes.
    if (currentRoute in bottomAppBarRoutes) {
        HubModalNavigationDrawer(
            darkTheme = darkTheme,
            onThemeUpdated = onThemeUpdated,
            navController = navController
        ) {
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
    MainScreen(
        darkTheme = true,
        onThemeUpdated = {},
        navController = rememberNavController(),
        mainViewModel = hiltViewModel(),
        counterService = CounterService()
    )
}