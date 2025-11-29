package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.routes.ComfyUI
import it.giovanni.hub.navigation.routes.ComfyUIRoutes
import it.giovanni.hub.presentation.screen.detail.comfyui.HairColorHistoryScreen
import it.giovanni.hub.presentation.screen.detail.comfyui.HairColorScreen
import it.giovanni.hub.presentation.screen.detail.comfyui.TextToImageHistoryScreen
import it.giovanni.hub.presentation.screen.detail.comfyui.TextToImageScreen
import it.giovanni.hub.presentation.screen.main.ComfyUIScreen
import it.giovanni.hub.presentation.viewmodel.comfyui.ComfyUIViewModel

fun NavGraphBuilder.comfyUINavGraph(
    navController: NavHostController,
    comfyUIViewModel: ComfyUIViewModel
    ) {
    navigation<ComfyUI>(
        startDestination = ComfyUIRoutes.ComfyUIRoute
    ) {
        composable<ComfyUIRoutes.ComfyUIRoute> {
            ComfyUIScreen(
                navController = navController,
                comfyUIViewModel = comfyUIViewModel
            )
        }

        composable<ComfyUIRoutes.TextToImage> {
            TextToImageScreen(
                navController = navController,
                comfyUIViewModel = comfyUIViewModel
            )
        }

        composable<ComfyUIRoutes.TextToImageHistory> {
            TextToImageHistoryScreen(
                navController = navController,
                comfyUIViewModel = comfyUIViewModel
            )
        }

        composable<ComfyUIRoutes.HairColor> {
            HairColorScreen(
                navController = navController,
                comfyUIViewModel = comfyUIViewModel
            )
        }

        composable<ComfyUIRoutes.HairColorHistory> {
            HairColorHistoryScreen(
                navController = navController,
                comfyUIViewModel = comfyUIViewModel
            )
        }
    }
}