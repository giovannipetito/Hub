package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Graph.HOME_ROUTE
import it.giovanni.hub.navigation.util.routes.MainRoutes
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    navigation(
        route = HOME_ROUTE,
        startDestination = MainRoutes.Home.route
    ) {
        composable(
            route = MainRoutes.Home.route
        ) {
            HomeScreen(navController = navController, mainViewModel = mainViewModel)
        }
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