package it.giovanni.hub.navigation.util.set

sealed class TopAppBarsSet(val route: String) {
    data object HubTopAppBars: TopAppBarsSet(route = "top_appbars_screen")
    data object HubTopAppBar: TopAppBarsSet(route = "top_appbar_screen")
    data object HubCenterAlignedTopAppBar: TopAppBarsSet(route = "center_aligned_top_appbar_screen")
    data object HubMediumTopAppBar: TopAppBarsSet(route = "medium_top_appbar_screen")
    data object HubLargeTopAppBar: TopAppBarsSet(route = "large_top_appbar_screen")
    data object HubSearchTopAppBar: TopAppBarsSet(route = "search_top_appbar_screen")
    data object HubCollapsingTopAppBar: TopAppBarsSet(route = "collapsing_top_appbar_screen")
}