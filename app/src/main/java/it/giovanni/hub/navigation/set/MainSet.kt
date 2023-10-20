package it.giovanni.hub.navigation.set

import it.giovanni.hub.Constants.DETAIL_ARG_KEY1
import it.giovanni.hub.Constants.DETAIL_ARG_KEY2

sealed class MainSet(val route: String) {

    object Home: MainSet(route = BottomBarSet.Home.route)

    object Detail1: MainSet(route = "detail1_screen/{$DETAIL_ARG_KEY1}/{$DETAIL_ARG_KEY2}") { // We are passing required arguments
        fun passRequiredArguments(id: Int, name: String): String {
            return "detail1_screen/$id/$name"
        }
    }

    object Detail2: MainSet(route = "detail2_screen?id={$DETAIL_ARG_KEY1}&name={$DETAIL_ARG_KEY2}") { // // We are passing optional arguments
        fun passOptionalArguments(id: Int = 0, name: String = ""): String {
            return "detail2_screen?id=$id&name=$name"
        }
    }

    object TextFields: MainSet(route = "text_fields")
    object Users: MainSet(route = "users")
    object UsersRx: MainSet(route = "users_rx")
    object UI: MainSet(route = "ui")
    object AnimatedShimmer: MainSet(route = "animated_shimmer")
}