package it.giovanni.hub.utils

enum class ColumnType {
    Column1,
    Column2
}

enum class RowType {
    Row1,
    Row2
}

enum class GridType {
    VerticalGrid1,
    VerticalGrid2,
    VerticalGrid3,
    VerticalGrid4,
    HorizontalGrid1,
    HorizontalGrid2,
    HorizontalGrid3,
    HorizontalGrid4
}

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

enum class CounterState {
    Idle,
    Started,
    Stopped,
    Canceled
}

enum class AlertBarPosition {
    TOP,
    BOTTOM
}

enum class SwipeActionType {
    Email,
    Share,
    Favorite,
    Info,
    Edit,
    Delete
}