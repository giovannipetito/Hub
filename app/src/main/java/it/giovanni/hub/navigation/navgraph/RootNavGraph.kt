package it.giovanni.hub.navigation.navgraph

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.giovanni.hub.navigation.routes.Loading
import it.giovanni.hub.navigation.routes.Wizard
import it.giovanni.hub.presentation.screen.main.LoadingScreen
import it.giovanni.hub.presentation.screen.main.WizardScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.presentation.viewmodel.PersonViewModel
import it.giovanni.hub.presentation.viewmodel.comfyui.ComfyUIViewModel

@Composable
fun RootNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    credentialManager: CredentialManager
) {
    // SharedViewModels
    val personViewModel: PersonViewModel = viewModel()
    val comfyUIViewModel: ComfyUIViewModel = viewModel()

    // Root Navigation Graph
    NavHost(
        navController = navController,
        startDestination = Loading,
        /*
        enterTransition = { fadeIn(animationSpec = tween(700)) },
        exitTransition = { fadeOut(animationSpec = tween(700)) },
        popEnterTransition = { fadeIn(animationSpec = tween(700)) },
        popExitTransition = { fadeOut(animationSpec = tween(700)) }
        */
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700)) }
    ) {
        composable<Loading> {
            LoadingScreen(
                navController = navController,
                mainViewModel = mainViewModel,
                onSplashLoaded = {
                    mainViewModel.setSplashOpened(state = false)
                }
            )
        }

        composable<Wizard> {
            WizardScreen(navController = navController)
        }

        loginNavGraph(
            navController = navController,
            mainViewModel = mainViewModel,
            credentialManager = credentialManager
        )

        // Nested Navigation Graphs
        homeNavGraph(
            navController = navController,
            mainViewModel = mainViewModel
        )

        profileNavGraph(
            navController = navController,
            mainViewModel = mainViewModel,
            personViewModel = personViewModel,
            comfyUIViewModel = comfyUIViewModel
        )

        settingsNavGraph(
            navController = navController,
            mainViewModel = mainViewModel
        )
    }
}