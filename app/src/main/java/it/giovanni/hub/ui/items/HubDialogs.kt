package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.DialogProperties

@Composable
fun PermissionDialog(rationaleMessage: String, onRequestPermission: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        icon = { Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "Info"
        )},
        title = {
            Text(
                text = "Permission Request",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Text(rationaleMessage)
        },
        // dismissButton = {},
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("Give Permission")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Composable
fun InfoDialog(topics: List<String>, showDialog: MutableState<Boolean>) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            icon = { Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Info"
            )},
            title = { Text("Topics") },
            text = {
                Column {
                    topics.forEach { topic ->
                        Text(text = topic)
                    }
                }
            },
            // dismissButton = {},
            confirmButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Close")
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    }
}