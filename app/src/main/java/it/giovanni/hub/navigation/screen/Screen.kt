package it.giovanni.hub.navigation.screen

import it.giovanni.hub.Constants.DETAIL_ARG_KEY1
import it.giovanni.hub.Constants.DETAIL_ARG_KEY2

sealed class Screen(val route: String) {

    object Home: Screen(route = BottomBarScreen.Home.route)

    object Detail1: Screen(route = "detail1_screen/{$DETAIL_ARG_KEY1}/{$DETAIL_ARG_KEY2}") { // We are passing required arguments
        fun passRequiredArguments(id: Int, name: String): String {
            return "detail1_screen/$id/$name"
        }
    }

    object Detail2: Screen(route = "detail2_screen?id={$DETAIL_ARG_KEY1}&name={$DETAIL_ARG_KEY2}") { // // We are passing optional arguments
        fun passOptionalArguments(id: Int = 0, name: String = ""): String {
            return "detail2_screen?id=$id&name=$name"
        }
    }

    object Auth: Screen(route = "auth_screen")
    object SignUp: Screen(route = "signup_screen")
    object TextFields: Screen(route = "text_fields")
    object Users: Screen(route = "users")
    object UsersRx: Screen(route = "users_rx")
    object UI: Screen(route = "ui")
}