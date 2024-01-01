package it.giovanni.hub.navigation.util.set

import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY1
import it.giovanni.hub.utils.Constants.DETAIL_ARG_KEY2

sealed class MainSet(val route: String) {

    data object Home: MainSet(route = BottomAppBarSet.Home.route)

    data object Profile: MainSet(route = BottomAppBarSet.Profile.route)

    data object Settings: MainSet(route = BottomAppBarSet.Settings.route)

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
    data object Colors: MainSet(route = "colors")
    data object Fonts: MainSet(route = "fonts")
    data object Buttons: MainSet(route = "buttons")
    data object Columns: MainSet(route = "columns")
    data object Rows: MainSet(route = "rows")
    data object Texts: MainSet(route = "texts")
    data object TextFields: MainSet(route = "text_fields")
    data object Cards: MainSet(route = "cards")
    data object UsersCoroutines: MainSet(route = "users_coroutines")
    data object UsersRxJava: MainSet(route = "users_rxjava")
    data object UI: MainSet(route = "ui")
    data object Slider: MainSet(route = "slider")
    data object PhotoPicker: MainSet(route = "photo_picker")
    data object Shimmer: MainSet(route = "shimmer")
    data object Shuffled: MainSet(route = "shuffled")
    data object Paging: MainSet(route = "paging")
    data object Hyperlink: MainSet(route = "hyperlink")
    data object SinglePermission: MainSet(route = "single_permission")
    data object MultiplePermissions: MainSet(route = "multiple_permissions")
    data object WebView: MainSet(route = "web_view")
    data object CounterService: MainSet(route = "counter_service")
    data object HorizontalPager: MainSet(route = "horizontal_pager")
    data object ProgressIndicators: MainSet(route = "progress_indicators")
    data object Chips: MainSet(route = "chips")
}