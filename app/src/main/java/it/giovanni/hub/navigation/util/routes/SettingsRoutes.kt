package it.giovanni.hub.navigation.util.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class SettingsRoutes {
    @Serializable
    data object Colors: SettingsRoutes()
    @Serializable
    data object Fonts: SettingsRoutes()
    @Serializable
    data object Buttons: SettingsRoutes()
    @Serializable
    data object Columns: SettingsRoutes()
    @Serializable
    data object Rows: SettingsRoutes()
    @Serializable
    data object Grids: SettingsRoutes()
    @Serializable
    data object Texts: SettingsRoutes()
    @Serializable
    data object TextFields: SettingsRoutes()
    @Serializable
    data object Cards: SettingsRoutes()
    @Serializable
    data object UI: SettingsRoutes()
    @Serializable
    data object Slider: SettingsRoutes()
    @Serializable
    data object PhotoPicker: SettingsRoutes()
    @Serializable
    data object Shimmer: SettingsRoutes()
    @Serializable
    data object Shuffled: SettingsRoutes()
    @Serializable
    data object HorizontalPager: SettingsRoutes()
    @Serializable
    data object AlertBar: SettingsRoutes()
    @Serializable
    data object TextToSpeech: SettingsRoutes()
    @Serializable
    data object SpeechToText: SettingsRoutes()
    @Serializable
    data object DragAndDrop: SettingsRoutes()
    @Serializable
    data object DateTime: SettingsRoutes()
    @Serializable
    data object Pane: SettingsRoutes()
}