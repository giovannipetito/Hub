package it.giovanni.hub.navigation.util.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class GeminiRoutes {
    @Serializable
    data object HubGemini: GeminiRoutes()
    @Serializable
    data object Multimodal: GeminiRoutes()
    @Serializable
    data object Chat: GeminiRoutes()
}