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
import it.giovanni.hub.data.model.GridItem
import it.giovanni.hub.data.model.Person

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
                "https://picsum.photos/id/10/300",
                "https://picsum.photos/id/12/300",
                "https://picsum.photos/id/15/300",
                "https://picsum.photos/id/25/300",
                "https://picsum.photos/id/28/300",
                "https://picsum.photos/id/37/300",
                "https://picsum.photos/id/43/300",
                "https://picsum.photos/id/49/300",
                "https://picsum.photos/id/54/300",
                "https://picsum.photos/id/56/300",
                "https://picsum.photos/id/58/300",
                "https://picsum.photos/id/69/300",
                "https://picsum.photos/id/70/300",
                "https://picsum.photos/id/71/300",
                "https://picsum.photos/id/74/300",
                "https://picsum.photos/id/77/300",
                "https://picsum.photos/id/82/300",
                "https://picsum.photos/id/84/300",
                "https://picsum.photos/id/89/300",
                "https://picsum.photos/id/98/300"
            )
        }
        return photos
    }

    @Composable
    fun getGridItems(): List<GridItem> {
        val gridItems: List<GridItem> = remember {
            mutableStateListOf(
                GridItem("1", "https://picsum.photos/id/10/300"),
                GridItem("2", "https://picsum.photos/id/12/300"),
                GridItem("3", "https://picsum.photos/id/15/300"),
                GridItem("4", "https://picsum.photos/id/25/300"),
                GridItem("5", "https://picsum.photos/id/28/300"),
                GridItem("6", "https://picsum.photos/id/37/300"),
                GridItem("7", "https://picsum.photos/id/43/300"),
                GridItem("8", "https://picsum.photos/id/49/300"),
                GridItem("9", "https://picsum.photos/id/54/300"),
                GridItem("10", "https://picsum.photos/id/56/300"),
                GridItem("11", "https://picsum.photos/id/58/300"),
                GridItem("13", "https://picsum.photos/id/69/300"),
                GridItem("14", "https://picsum.photos/id/70/300"),
                GridItem("15", "https://picsum.photos/id/71/300"),
                GridItem("16", "https://picsum.photos/id/74/300"),
                GridItem("17", "https://picsum.photos/id/77/300"),
                GridItem("18", "https://picsum.photos/id/82/300"),
                GridItem("19", "https://picsum.photos/id/84/300"),
                GridItem("20", "https://picsum.photos/id/89/300")
            )
        }
        return gridItems
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

    fun getMockedContacts(): List<Person> {
        return listOf(
            Person(id = 1, firstName = "Dario", lastName = "Barone", true),
            Person(id = 2, firstName = "Angela", lastName = "Basile", true),
            Person(id = 3, firstName = "Elena", lastName = "Basile", true),
            Person(id = 4, firstName = "Franco", lastName = "Basile", true),
            Person(id = 5, firstName = "Giovanni", lastName = "Basile", true),
            Person(id = 6, firstName = "Marco", lastName = "Basile", true),
            Person(id = 7, firstName = "Tina", lastName = "Basile", true),
            Person(id = 8, firstName = "Tommaso", lastName = "Basile", true),
            Person(id = 9, firstName = "Francesca", lastName = "Carpentieri", true),
            Person(id = 10, firstName = "Santina", lastName = "Ciccarelli", true),
            Person(id = 11, firstName = "Lorenzo", lastName = "Corti", true),
            Person(id = 12, firstName = "Antonio", lastName = "D'Ascia", true),
            Person(id = 13, firstName = "Davide", lastName = "D'Ascia", true),
            Person(id = 14, firstName = "Giovanni", lastName = "D\'Ascia", true),
            Person(id = 15, firstName = "Sofia", lastName = "D'Ascia", true),
            Person(id = 16, firstName = "Flavia", lastName = "Ferrara", true),
            Person(id = 17, firstName = "Stefano", lastName = "Mariani", true),
            Person(id = 18, firstName = "Antonio", lastName = "Mariniello", true),
            Person(id = 19, firstName = "Elsa", lastName = "Mazzarella", true),
            Person(id = 20, firstName = "Nicola", lastName = "Migliaccio", true),
            Person(id = 21, firstName = "Daniele", lastName = "Musacchia", true),
            Person(id = 22, firstName = "Roberta", lastName = "Normano", true),
            Person(id = 23, firstName = "Martina", lastName = "Pedrazzoli", true),
            Person(id = 24, firstName = "Giovanni", lastName = "Petito", true),
            Person(id = 25, firstName = "Raffaele", lastName = "Petito", true),
            Person(id = 26, firstName = "Teresa", lastName = "Petito", true),
            Person(id = 27, firstName = "Vincenzo", lastName = "Petito", true),
            Person(id = 28, firstName = "Mariano", lastName = "Pinto", true),
            Person(id = 29, firstName = "Armando", lastName = "Pragliola", true),
            Person(id = 30, firstName = "Ilenia", lastName = "Pragliola", true),
            Person(id = 31, firstName = "Salvatore", lastName = "Pragliola", true),
            Person(id = 32, firstName = "Una", lastName = "Rosandic", true),
            Person(id = 33, firstName = "Gianluigi", lastName = "Santillo", true),
            Person(id = 34, firstName = "Susy", lastName = "Scala", true),
            Person(id = 35, firstName = "Sonia", lastName = "Strazzulli", true),
            Person(id = 36, firstName = "Tara", lastName = "Tandel", true),
            Person(id = 37, firstName = "Greta", lastName = "Tonelli", true),
            Person(id = 38, firstName = "Emmanuele", lastName = "Villa", true),
            Person(id = 39, firstName = "Elena", lastName = "Volpi", true),
            Person(id = 40, firstName = "Antonio", lastName = "Zaccaria", true)
        )
    }

}