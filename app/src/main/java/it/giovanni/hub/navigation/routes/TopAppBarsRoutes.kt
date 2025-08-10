package it.giovanni.hub.navigation.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class TopAppBarsRoutes {
    @Serializable
    data object HubTopAppBars: TopAppBarsRoutes()
    @Serializable
    data object HubTopAppBar: TopAppBarsRoutes()
    @Serializable
    data object HubCenterAlignedTopAppBar: TopAppBarsRoutes()
    @Serializable
    data object HubMediumTopAppBar: TopAppBarsRoutes()
    @Serializable
    data object HubLargeTopAppBar: TopAppBarsRoutes()
    @Serializable
    data object HubCollapsingTopAppBar: TopAppBarsRoutes()
}