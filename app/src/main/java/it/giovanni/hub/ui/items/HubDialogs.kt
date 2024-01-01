package it.giovanni.hub.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SimpleDialog(showDialog: MutableState<Boolean>, onDismissRequest: () -> Unit) {
    if (showDialog.value) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "Simple Dialog",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HubAlertDialog(
    showDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    when {
        showDialog.value -> {
            AlertDialog(
                icon = {
                    Icon(Icons.Default.Info, contentDescription = "Alert Dialog Icon")
                },
                title = {
                    Text(text = "Alert Dialog")
                },
                text = {
                    Text(text = "This is an Alert Dialog with buttons.")
                },
                onDismissRequest = {
                    onDismissRequest()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirmation()
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}

@Composable
fun ImageDialog(
    showDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter
) {
    if (showDialog.value) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .height(160.dp)
                            .clip(CircleShape),
                        painter = painter,
                        contentScale = ContentScale.Fit,
                        contentDescription = "Image Dialog"
                    )
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "This is an Image Dialog with buttons."
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            modifier = Modifier.padding(8.dp),
                            onClick = { onDismissRequest() }
                        ) {
                            Text("Dismiss")
                        }
                        TextButton(
                            modifier = Modifier.padding(8.dp),
                            onClick = { onConfirmation() }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoDialog(
    topics: List<String>,
    showDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            icon = { Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Info"
            )},
            title = { Text("Topics") },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        topics.forEach { topic ->
                            Text(text = topic)
                        }
                    }
                }
            },
            onDismissRequest = { onDismissRequest() },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Dismiss")
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        )
    }
}

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