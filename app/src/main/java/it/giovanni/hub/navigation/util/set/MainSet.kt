package it.giovanni.hub.navigation.util.set

import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY1
import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY2

sealed class MainSet(val route: String) {

    data object Home: MainSet(route = BottomBarSet.Home.route)

    data object Profile: MainSet(route = BottomBarSet.Profile.route)

    data object Settings: MainSet(route = BottomBarSet.Settings.route)

    data object Detail1: MainSet(route = "detail1/{$DETAIL_ARG_KEY1}/{$DETAIL_ARG_KEY2}") { // We are passing required arguments
        fun passRequiredArguments(id: Int, name: String): String {
            return "detail1/$id/$name"
        }
    }

    data object Detail2: MainSet(route = "detail2?id={$DETAIL_ARG_KEY1}&name={$DETAIL_ARG_KEY2}") { // // We are passing optional arguments
        fun passOptionalArguments(id: Int = 0, name: String = ""): String {
            return "detail2?id=$id&name=$name"
        }
    }

    data object Detail3: MainSet(route = "detail3")
    data object Detail4: MainSet(route = "detail4")
    data object PersonState: MainSet(route = "person_state")
    data object Texts: MainSet(route = "texts")
    data object TextFields: MainSet(route = "text_fields")
    data object Users: MainSet(route = "users")
    data object UsersRx: MainSet(route = "users_rx")
    data object UI: MainSet(route = "ui")
    data object Shimmer: MainSet(route = "shimmer")
    data object Shuffled: MainSet(route = "shuffled")
    data object Paging: MainSet(route = "paging")
    data object Hyperlink: MainSet(route = "hyperlink")
    data object SinglePermission: MainSet(route = "single_permission")
    data object MultiplePermissions: MainSet(route = "multiple_permissions")
    data object WebView: MainSet(route = "webview")
    data object TopBar: MainSet(route = "topbar")
    data object CollapsingTopBar: MainSet(route = "collapsing_topbar")
}