package it.giovanni.hub.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class ComfyUIRoutes {
    @Serializable
    data object ComfyUIRoute: ComfyUIRoutes()
    @Serializable
    data object TextToImage: ComfyUIRoutes()
    @Serializable
    data object HairColor: ComfyUIRoutes()
}