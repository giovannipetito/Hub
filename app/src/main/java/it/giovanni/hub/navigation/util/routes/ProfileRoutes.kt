package it.giovanni.hub.navigation.util.routes

import it.giovanni.hub.utils.Constants

sealed class ProfileRoutes(val route: String) {

    data object Detail1: ProfileRoutes(route = "detail1/{${Constants.DETAIL_ARG_KEY1}}/{${Constants.DETAIL_ARG_KEY2}}") { // We are passing required arguments
        fun passRequiredArguments(id: Int, name: String): String {
            return "detail1/$id/$name"
        }
    }
    data object Detail2: ProfileRoutes(route = "detail2?id={${Constants.DETAIL_ARG_KEY1}}&name={${Constants.DETAIL_ARG_KEY2}}") { // // We are passing optional arguments
        fun passOptionalArguments(id: Int = 0, name: String = ""): String {
            return "detail2?id=$id&name=$name"
        }
    }
    data object Detail3: ProfileRoutes(route = "detail3")
    data object Detail4: ProfileRoutes(route = "detail4")
    data object PersonState: ProfileRoutes(route = "person_state")
    data object Contacts: ProfileRoutes(route = "contacts")
    data object Header: ProfileRoutes(route = "header")
    data object StickyHeader: ProfileRoutes(route = "sticky_header")
    data object SwipeActions: ProfileRoutes(route = "swipe_actions")
    data object UsersCoroutines: ProfileRoutes(route = "users_coroutines")
    data object UsersRxJava: ProfileRoutes(route = "users_rxjava")
    data object Paging: ProfileRoutes(route = "paging")
    data object SinglePermission: ProfileRoutes(route = "single_permission")
    data object MultiplePermissions: ProfileRoutes(route = "multiple_permissions")
    data object WebView: ProfileRoutes(route = "web_view")
    data object CounterService: ProfileRoutes(route = "counter_service")
    data object ErrorHandling: ProfileRoutes(route = "error_handling")
}