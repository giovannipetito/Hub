package it.giovanni.hub.navigation

import it.giovanni.hub.Constants.DETAIL_ARG_KEY1
import it.giovanni.hub.Constants.DETAIL_ARG_KEY2

sealed class Screen(val route: String) {

    object Home: Screen(route = "home_screen")

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

    object Login: Screen(route = "login_screen")
    object SignUp: Screen(route = "signup_screen")
    object TextFields: Screen(route = "text_fields")
    object Users: Screen(route = "users")
}