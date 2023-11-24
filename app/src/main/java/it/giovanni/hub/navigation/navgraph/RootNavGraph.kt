package it.giovanni.hub.navigation.navgraph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.navigation.Graph.MAIN_ROUTE
import it.giovanni.hub.navigation.Graph.WIZARD_ROUTE
import it.giovanni.hub.navigation.Graph.ROOT_ROUTE
import it.giovanni.hub.navigation.Graph.LOADING_ROUTE
import it.giovanni.hub.presentation.screen.main.MainScreen
import it.giovanni.hub.presentation.screen.main.LoadingScreen
import it.giovanni.hub.presentation.screen.main.WizardScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.utils.Globals.getNavigationBarPadding

@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    /**
     * By calling enableEdgeToEdge method in MainActivity, I made my app display edge-to-edge
     * (using the entire width and height of the display) by drawing behind the system bars.
     * The system bars are the status bar and the navigation bar.
     *
     * After I enabled the edge-to-edge display, some of my app's views appeared behind the
     * navigation bar, such as the BottomBar. So I introduced the bottomPadding by calling
     * getNavigationBarPadding method to handle overlaps using insets.
     */

    // Root Navigation Graph
    NavHost(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background).padding(bottom = getNavigationBarPadding()),
        navController = navController,
        route = ROOT_ROUTE,
        startDestination = LOADING_ROUTE
    ) {
        composable(route = LOADING_ROUTE) {
            LoadingScreen(
                navController = navController,
                onSplashLoaded = {
                    mainViewModel.setSplashOpened(state = false)
                }
            )
        }
        composable(route = WIZARD_ROUTE) {
            WizardScreen(navController = navController)
        }
        loginNavGraph(navController = navController, mainViewModel = mainViewModel)
        composable(route = MAIN_ROUTE) {
            MainScreen(navController = rememberNavController(), mainViewModel = mainViewModel)
        }
    }
}