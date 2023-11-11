package it.giovanni.hub.navigation.util.set

import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY1
import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY2

sealed class MainSet(val route: String) {

    data object Home: MainSet(route = BottomBarSet.Home.route)

    data object Detail1: MainSet(route = "detail1_screen/{$DETAIL_ARG_KEY1}/{$DETAIL_ARG_KEY2}") { // We are passing required arguments
        fun passRequiredArguments(id: Int, name: String): String {
            return "detail1_screen/$id/$name"
        }
    }

    data object Detail2: MainSet(route = "detail2_screen?id={$DETAIL_ARG_KEY1}&name={$DETAIL_ARG_KEY2}") { // // We are passing optional arguments
        fun passOptionalArguments(id: Int = 0, name: String = ""): String {
            return "detail2_screen?id=$id&name=$name"
        }
    }

    data object Detail3: MainSet(route = "detail3_screen")
    data object Detail4: MainSet(route = "detail4_screen")
    data object TextFields: MainSet(route = "text_fields")
    data object Users: MainSet(route = "users")
    data object UsersRx: MainSet(route = "users_rx")
    data object UI: MainSet(route = "ui")
    data object Shimmer: MainSet(route = "shimmer")
    data object Shuffled: MainSet(route = "shuffled")
    data object Paging: MainSet(route = "paging")
    data object Permissions: MainSet(route = "permissions")
    data object SinglePermission: MainSet(route = "single_permission")
    data object MultiplePermissions: MainSet(route = "multiple_permissions")
}