package it.giovanni.hub.navigation.util.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class ProfileRoutes {
    @Serializable
    data class Detail1(
        // We are passing required arguments
        val id: Int,
        val name: String
    ): ProfileRoutes()
    @Serializable
    data class Detail2(
        // We are passing optional arguments
        val id: Int,
        val name: String
    ): ProfileRoutes()
    @Serializable
    data object Detail3: ProfileRoutes()
    @Serializable
    data object Detail4: ProfileRoutes()
    @Serializable
    data object PersonState: ProfileRoutes()
    @Serializable
    data object Contacts: ProfileRoutes()
    @Serializable
    data object Header: ProfileRoutes()
    @Serializable
    data object StickyHeader: ProfileRoutes()
    @Serializable
    data object SwipeActions: ProfileRoutes()
    @Serializable
    data object UsersCoroutines: ProfileRoutes()
    @Serializable
    data object UsersRxJava: ProfileRoutes()
    @Serializable
    data object PullToRefresh: ProfileRoutes()
    @Serializable
    data object Paging: ProfileRoutes()
    @Serializable
    data object SinglePermission: ProfileRoutes()
    @Serializable
    data object MultiplePermissions: ProfileRoutes()
    @Serializable
    data object WebView: ProfileRoutes()
    @Serializable
    data object CounterService: ProfileRoutes()
    @Serializable
    data object ErrorHandling: ProfileRoutes()
}