package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.Gemini
import it.giovanni.hub.navigation.util.routes.GeminiRoutes
import it.giovanni.hub.presentation.screen.detail.gemini.ChatScreen
import it.giovanni.hub.presentation.screen.detail.gemini.MultimodalScreen
import it.giovanni.hub.presentation.screen.main.GeminiScreen

fun NavGraphBuilder.geminiNavGraph(navController: NavHostController) {
    navigation<Gemini>(
        startDestination = GeminiRoutes.HubGemini
    ) {
        composable<GeminiRoutes.HubGemini> {
            GeminiScreen(navController = navController)
        }

        composable<GeminiRoutes.Multimodal> {
            MultimodalScreen(navController = navController)
        }

        composable<GeminiRoutes.Chat> {
            ChatScreen(navController = navController)
        }
    }
}