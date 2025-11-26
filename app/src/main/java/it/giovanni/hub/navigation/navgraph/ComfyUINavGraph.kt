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

fun NavGraphBuilder.comfyUINavGraph(navController: NavHostController) {
    navigation<ComfyUI>(
        startDestination = ComfyUIRoutes.ComfyUIRoute
    ) {
        composable<ComfyUIRoutes.ComfyUIRoute> {
            ComfyUIScreen(navController = navController)
        }

        composable<ComfyUIRoutes.TextToImage> {
            TextToImageScreen(navController = navController)
        }

        composable<ComfyUIRoutes.TextToImageHistory> {
            TextToImageHistoryScreen(navController = navController)
        }

        composable<ComfyUIRoutes.HairColor> {
            HairColorScreen(navController = navController)
        }

        composable<ComfyUIRoutes.HairColorHistory> {
            HairColorHistoryScreen(navController = navController)
        }
    }
}