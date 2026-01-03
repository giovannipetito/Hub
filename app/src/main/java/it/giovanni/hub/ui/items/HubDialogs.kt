package it.giovanni.hub.ui.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import it.giovanni.hub.presentation.model.StyleItem
import it.giovanni.hub.utils.Globals.getContentPadding
import it.giovanni.hub.utils.Globals.getTextFieldColors

@Composable
fun SimpleDialog(showDialog: MutableState<Boolean>, onDismissRequest: () -> Unit) {
    if (showDialog.value) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 200.dp)
                    .padding(all = 16.dp),
                shape = RoundedCornerShape(size = 16.dp),
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
    icon: Painter = infoIcon(),
    title: String = "Alert Dialog",
    text: String = "Alert Dialog with text and buttons.",
    dismissButtonText: String = "Dismiss",
    confirmButtonText: String = "Confirm",
    showDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    when {
        showDialog.value -> {
            AlertDialog(
                icon = {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = icon,
                        contentDescription = "Alert Dialog Icon"
                    )
                },
                title = {
                    Text(text = title)
                },
                text = {
                    Text(text = text)
                },
                onDismissRequest = {
                    onDismissRequest()
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text(text = dismissButtonText, color = Color.Red)
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirmation()
                        }
                    ) {
                        Text(text = confirmButtonText, color = Color.Green)
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
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
        Dialog(
            onDismissRequest = {
                onDismissRequest()
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 375.dp)
                    .padding(all = 16.dp),
                shape = RoundedCornerShape(size = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .height(height = 160.dp)
                            .clip(shape = CircleShape),
                        painter = painter,
                        contentScale = ContentScale.Fit,
                        contentDescription = "Image Dialog"
                    )
                    Text(
                        modifier = Modifier.padding(all = 16.dp),
                        text = "This is an Image Dialog with buttons."
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.padding(all = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ),
                            onClick = { onDismissRequest() }
                        ) {
                            Text(text = "Dismiss")
                        }
                        Button(
                            modifier = Modifier.padding(all = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green,
                                contentColor = Color.White
                            ),
                            onClick = { onConfirmation() }
                        ) {
                            Text(text = "Confirm")
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
    onConfirmation: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            icon = {
                Icon(
                    modifier = Modifier.size(size = 24.dp),
                    painter = infoIcon(),
                    contentDescription = "Info Icon"
                )
            },
            title = { Text(text = "Topics") },
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
            onDismissRequest = {},
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(text = "Close")
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
fun ListDialog(
    title: String,
    list: List<String>,
    confirmButtonText: String,
    showDialog: MutableState<Boolean>,
    onConfirmation: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            modifier = Modifier.graphicsLayer(alpha = 0.8f), // 20% transparent
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        list.forEach { item ->
                            Text(text = item)
                        }
                    }
                }
            },
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(text = confirmButtonText)
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
fun ClickableListDialog(
    title: String,
    list: List<String>,
    confirmButtonText: String,
    showDialog: MutableState<Boolean>,
    onConfirmation: () -> Unit,
    itemClickActions: List<() -> Unit>
) {
    if (showDialog.value) {
        AlertDialog(
            modifier = Modifier,
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    itemsIndexed(list) { index, item ->
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { itemClickActions[index]() }
                                .padding(vertical = 8.dp),
                            text = item
                        )
                    }
                }
            },
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(text = confirmButtonText)
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
        icon = {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                painter = infoIcon(),
                contentDescription = "Info Icon"
            )
        },
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
            Text(text = rationaleMessage)
        },
        onDismissRequest = {},
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text(text = "Give Permission")
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Composable
fun TextFieldDialog(
    icon: Painter = infoIcon(),
    title: String = "Alert Dialog",
    text: String = "Alert Dialog with text and buttons.",
    message: MutableState<TextFieldValue>,
    dismissButtonText: String = "Dismiss",
    confirmButtonText: String = "Confirm",
    showDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    when {
        showDialog.value -> {
            AlertDialog(
                icon = {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = icon,
                        contentDescription = "Alert Dialog Icon"
                    )
                },
                title = {
                    Text(text = title)
                },
                text = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                                shape = RoundedCornerShape(size = 48.dp),
                                value = message.value,
                                placeholder = { Text(text = "Enter the message") },
                                onValueChange = { input -> message.value = input },
                                singleLine = true,
                                colors = getTextFieldColors()
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(height = 12.dp))
                            Text(
                                text = text,
                                color =
                                if (message.value.text.isNotEmpty())
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Transparent
                            )
                        }
                    }
                },
                onDismissRequest = {
                    onDismissRequest()
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text(text = dismissButtonText, color = Color.Red)
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirmation()
                        },
                        enabled = message.value.text.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Green,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Green.copy(alpha = 0.5f)
                        ),
                    ) {
                        Text(text = confirmButtonText)
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
        }
    }
}

@Composable
fun TextFieldsDialog(
    icon: Painter = infoIcon(),
    title: String = "Alert Dialog",
    text: String = "Alert Dialog with text and buttons.",
    firstName: MutableState<TextFieldValue>,
    lastName: MutableState<TextFieldValue>,
    age: MutableState<TextFieldValue>,
    dismissButtonText: String = "Dismiss",
    confirmButtonText: String = "Confirm",
    showDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    when {
        showDialog.value -> {
            AlertDialog(
                icon = {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = icon,
                        contentDescription = "Alert Dialog Icon"
                    )
                },
                title = {
                    Text(text = title)
                },
                text = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                                shape = RoundedCornerShape(size = 48.dp),
                                value = firstName.value,
                                placeholder = { Text(text = "Enter the name") },
                                onValueChange = { input -> firstName.value = input },
                                singleLine = true,
                                colors = getTextFieldColors()
                            )
                        }
                        item {
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                                shape = RoundedCornerShape(size = 48.dp),
                                value = lastName.value,
                                placeholder = { Text(text = "Enter the surname") },
                                onValueChange = { input -> lastName.value = input },
                                singleLine = true,
                                colors = getTextFieldColors()
                            )
                        }
                        item {
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .wrapContentHeight(align = Alignment.CenterVertically),
                                shape = RoundedCornerShape(size = 48.dp),
                                value = age.value,
                                placeholder = { Text(text = "Enter the age") },
                                onValueChange = { input -> age.value = input },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = getTextFieldColors()
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(height = 12.dp))
                            Text(
                                text = text,
                                color =
                                if (firstName.value.text.isNotEmpty() && lastName.value.text.isNotEmpty() && age.value.text.isNotEmpty())
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color.Transparent
                            )
                        }
                    }
                },
                onDismissRequest = {
                    onDismissRequest()
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text(text = dismissButtonText, color = Color.Red)
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirmation()
                        },
                        enabled = firstName.value.text.isNotEmpty() && lastName.value.text.isNotEmpty() && age.value.text.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Green,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Green.copy(alpha = 0.5f)
                        ),
                    ) {
                        Text(text = confirmButtonText)
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
        }
    }
}

@Composable
fun StylesDialog(
    icon: Painter = audioslaveIcon(),
    title: String = "Styles Dialog",
    showDialog: MutableState<Boolean>,
    onDismissRequest: () -> Unit,
    onConfirmation: (StyleItem) -> Unit,
    styleItems: List<StyleItem>
) {
    if (!showDialog.value) return

    var selectedItem by remember(styleItems) { mutableStateOf<StyleItem?>(null) }

    AlertDialog(
        icon = {
            Image(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = "Styles Dialog Icon"
            )
        },
        title = { Text(text = title) },
        onDismissRequest = onDismissRequest,
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 240.dp, max = 520.dp)
            ) {
                VerticalGridStyles(
                    styleItems = styleItems,
                    paddingValues = PaddingValues(0.dp),
                    selectedItem = selectedItem,
                    onItemSelected = { selectedItem = it }
                )
            }
        },
        dismissButton = {},
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedItem?.let { item ->
                    TextButton(onClick = { onConfirmation(item) }) {
                        Text("Confirm", color = MaterialTheme.colorScheme.primary)
                    }
                }

                TextButton(onClick = onDismissRequest) {
                    Text("Dismiss", color = Color.Red)
                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Composable
fun VerticalGridStyles(
    styleItems: List<StyleItem>,
    paddingValues: PaddingValues,
    selectedItem: StyleItem?,
    onItemSelected: (StyleItem) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 96.dp),
        contentPadding = getContentPadding(paddingValues = paddingValues),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = styleItems,
            key = { it.styleName }
        ) { item ->
            StyleItemAsyncImage(
                styleItem = item,
                isSelected = (item == selectedItem),
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Composable
fun StyleItemAsyncImage(
    styleItem: StyleItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(2.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = styleItem.imageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "StyleItem with name ${styleItem.styleName}"
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            text = styleItem.styleName,
            color = Color.White
        )

        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }

        // Favorite marker
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(16.dp)
                    .clip(shape)
                    .border(
                        border = BorderStroke(width = 1.dp, color = Color(0xFF4CAF50)),
                        shape = shape
                    )
                    .background(color = MaterialTheme.colorScheme.background)
            )
        }
    }
}