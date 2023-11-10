package it.giovanni.hub.utils

enum class SearchWidgetState {
    OPENED,
    CLOSED
}

/**
 * Small:  - indicates height in landscape mode and width in portrait mode of smartphone devices.
 * Medium: - indicates height in portrait mode and width in landscape mode of smartphone devices.
 *         - indicates height in landscape mode and width in portrait mode of tablet devices.
 * Large:  - indicates height in portrait mode and width in landscape mode of tablet devices.
 */
enum class ScreenType {
    Small,
    Medium,
    Large
}

enum class DeviceType {
    SmartphoneInPortraitMode,
    SmartphoneInLandscapeMode,
    TabletInPortraitMode,
    TabletInLandscapeMode
}