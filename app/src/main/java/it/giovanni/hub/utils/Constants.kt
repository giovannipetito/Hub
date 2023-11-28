package it.giovanni.hub.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

object Constants {

    // Argument key names
    const val DETAIL_ARG_KEY1: String = "id"
    const val DETAIL_ARG_KEY2: String = "name"

    val TOP_BAR_HEIGHT = 56.dp
    val BOTTOM_BAR_HEIGHT = 56.dp

    val icons: List<ImageVector> = listOf(
        Icons.Default.Home,
        Icons.Default.Face,
        Icons.Default.Email,
        Icons.Default.Call,
        Icons.Default.Check,
        Icons.Default.Edit
    )

    const val ACTION_SERVICE_START = "action_service_start"
    const val ACTION_SERVICE_STOP = "action_service_stop"
    const val ACTION_SERVICE_CANCEL = "action_service_cancel"

    const val COUNTER_STATE = "counter_state"

    const val NOTIFICATION_CHANNEL_ID = "counter_notification_id"
    const val NOTIFICATION_CHANNEL_NAME = "counter_notification"
    const val NOTIFICATION_ID = 10

    const val CLICK_REQUEST_CODE = 100
    const val CANCEL_REQUEST_CODE = 101
    const val STOP_REQUEST_CODE = 102
    const val RESUME_REQUEST_CODE = 103
}