package it.giovanni.hub.navigation.util.routes

sealed class TopAppBarsRoutes(val route: String) {

    data object HubTopAppBars: TopAppBarsRoutes(route = "top_appbars_screen")
    data object HubTopAppBar: TopAppBarsRoutes(route = "top_appbar_screen")
    data object HubCenterAlignedTopAppBar: TopAppBarsRoutes(route = "center_aligned_top_appbar_screen")
    data object HubMediumTopAppBar: TopAppBarsRoutes(route = "medium_top_appbar_screen")
    data object HubLargeTopAppBar: TopAppBarsRoutes(route = "large_top_appbar_screen")
    data object HubSearchTopAppBar: TopAppBarsRoutes(route = "search_top_appbar_screen")
    data object HubCollapsingTopAppBar: TopAppBarsRoutes(route = "collapsing_top_appbar_screen")
}