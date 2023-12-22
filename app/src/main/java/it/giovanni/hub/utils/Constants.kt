package it.giovanni.hub.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

object Constants {

    // Argument key names
    const val DETAIL_ARG_KEY1: String = "id"
    const val DETAIL_ARG_KEY2: String = "name"

    val TOP_BAR_HEIGHT = 64.dp

    val icons: List<ImageVector> = listOf(
        Icons.Default.Home,
        Icons.Default.Face,
        Icons.Default.Email,
        Icons.Default.Call,
        Icons.Default.Check,
        Icons.Default.Edit
    )

    @Composable
    fun getPhotos(): SnapshotStateList<String> {
        val photos: SnapshotStateList<String> = remember {
            mutableStateListOf(
                "https://picsum.photos/id/12/300",
                "https://picsum.photos/id/28/300",
                "https://picsum.photos/id/49/300",
                "https://picsum.photos/id/54/300",
                "https://picsum.photos/id/56/300",
                "https://picsum.photos/id/58/300",
                "https://picsum.photos/id/70/300",
                "https://picsum.photos/id/71/300",
                "https://picsum.photos/id/82/300",
                "https://picsum.photos/id/84/300",
            )
        }
        return photos
    }

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

    const val emailRegex: String = "(" +
            "([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+|" +
            "([a-zA-Z0-9]+_)+[a-zA-Z0-9]+|" +
            "([a-zA-Z0-9]+-)+[a-zA-Z0-9]+|" +
            "[a-zA-Z0-9]" +
            "){2,256}" +
            "@" +
            "[a-zA-Z0-9]{0,64}" +
            "(\\.[a-zA-Z0-9]{0,25}" +
            ")"

    const val passwordRegex: String = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]).{8,20}$"

    const val loremIpsumLongText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
            "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
            "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
            "fugiat nulla pariatur."

    const val loremIpsumShortText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed " +
            "do eiusmod tempor incididunt ut labore et dolore magna aliqua."
}