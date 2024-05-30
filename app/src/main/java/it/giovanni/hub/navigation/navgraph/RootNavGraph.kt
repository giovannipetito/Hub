package it.giovanni.hub.navigation.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.giovanni.hub.domain.GoogleAuthClient
import it.giovanni.hub.navigation.Loading
import it.giovanni.hub.navigation.Wizard
import it.giovanni.hub.presentation.screen.main.LoadingScreen
import it.giovanni.hub.presentation.screen.main.WizardScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

@ExperimentalAnimationApi
@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    googleAuthClient: GoogleAuthClient
) {
    val personViewModel: PersonViewModel = viewModel() // SharedViewModel

    // Root Navigation Graph
    NavHost(
        navController = navController,
        startDestination = Loading
    ) {
        composable<Loading> {
            LoadingScreen(
                navController = navController,
                onSplashLoaded = {
                    mainViewModel.setSplashOpened(state = false)
                },
                googleAuthClient = googleAuthClient
            )
        }

        composable<Wizard> {
            WizardScreen(navController = navController)
        }

        loginNavGraph(
            navController = navController,
            mainViewModel = mainViewModel,
            googleAuthClient = googleAuthClient
        )

        // Nested Navigation Graphs
        homeNavGraph(
            navController = navController,
            mainViewModel = mainViewModel,
            googleAuthClient = googleAuthClient
        )

        profileNavGraph(
            navController = navController,
            personViewModel = personViewModel
        )

        settingsNavGraph(navController = navController)
    }
}