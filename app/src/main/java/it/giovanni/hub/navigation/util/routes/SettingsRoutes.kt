package it.giovanni.hub.navigation.util.routes

sealed class SettingsRoutes(val route: String) {

    data object Colors: SettingsRoutes(route = "colors")
    data object Fonts: SettingsRoutes(route = "fonts")
    data object Buttons: SettingsRoutes(route = "buttons")
    data object Columns: SettingsRoutes(route = "columns")
    data object Rows: SettingsRoutes(route = "rows")
    data object Grids: SettingsRoutes(route = "grids")
    data object Texts: SettingsRoutes(route = "texts")
    data object TextFields: SettingsRoutes(route = "text_fields")
    data object Cards: SettingsRoutes(route = "cards")
    data object UI: SettingsRoutes(route = "ui")
    data object Slider: SettingsRoutes(route = "slider")
    data object PhotoPicker: SettingsRoutes(route = "photo_picker")
    data object Shimmer: SettingsRoutes(route = "shimmer")
    data object Shuffled: SettingsRoutes(route = "shuffled")
    data object HorizontalPager: SettingsRoutes(route = "horizontal_pager")
    data object ProgressIndicators: SettingsRoutes(route = "progress_indicators")
    data object Chips: SettingsRoutes(route = "chips")
    data object AlertBar: SettingsRoutes(route = "alert_bar")
}