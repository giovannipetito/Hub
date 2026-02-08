package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import it.giovanni.hub.data.entity.BirthdayEntity
import it.giovanni.hub.domain.birthday.DobVisualTransformation
import it.giovanni.hub.domain.birthday.formatDobDigits
import it.giovanni.hub.domain.birthday.isDobValidOrBlankDigits
import kotlin.text.isDigit

@Composable
fun ViewBirthdayDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    birthdays: List<BirthdayEntity>,
    onDismissRequest: () -> Unit
) {
    if (!showDialog.value) return

    AlertDialog(
        title = { Text(title) },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 340.dp)
            ) {
                if (birthdays.isEmpty()) {
                    item { Text("No birthdays.") }
                } else {
                    items(birthdays.size) { idx ->
                        val b = birthdays[idx]
                        val formattedDob = formatDobDigits(b.yearOfBirth)
                        ListItem(
                            headlineContent = { Text("${b.firstName} ${b.lastName}") },
                            supportingContent = { Text("Year: $formattedDob") }
                        )
                        if (idx < birthdays.lastIndex)
                            HorizontalDivider()
                    }
                }
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) { Text("Close") }
        }
    )
}

@Composable
fun AddEditBirthdayDialog(
    title: String,
    icon: Painter,
    confirmButtonText: String,
    showDialog: MutableState<Boolean>,
    firstName: MutableState<TextFieldValue>,
    lastName: MutableState<TextFieldValue>,
    yearOfBirth: MutableState<TextFieldValue>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    if (!showDialog.value) return

    AlertDialog(
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = "Birthday dialog icon"
            )
        },
        title = { Text(title) },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        value = firstName.value,
                        onValueChange = { firstName.value = it },
                        placeholder = { Text("First name") },
                        singleLine = true
                    )
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        value = lastName.value,
                        onValueChange = { lastName.value = it },
                        placeholder = { Text("Last name") },
                        singleLine = true
                    )
                }
                item {
                    val dobTransformation = remember { DobVisualTransformation() }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        value = yearOfBirth.value,
                        onValueChange = { newValue ->
                            val digitsOnly = newValue.text.filter { it.isDigit() }.take(8)
                            val newSel = newValue.selection.end.coerceAtMost(digitsOnly.length)
                            yearOfBirth.value = newValue.copy(text = digitsOnly, selection = TextRange(newSel))
                        },
                        label = { Text("Year of birth") },
                        placeholder = { Text("gg/mm/aaaa") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = dobTransformation
                    )
                }
            }
        },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Dismiss", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            val isButtonEnabled =
                firstName.value.text.isNotBlank() &&
                        lastName.value.text.isNotBlank() &&
                        isDobValidOrBlankDigits(yearOfBirth.value.text)
            TextButton(
                onClick = onConfirmation,
                enabled = isButtonEnabled
            ) { Text(confirmButtonText) }
        }
    )
}

@Composable
fun DeleteBirthdayDialog(
    showDeleteDialog: MutableState<Boolean>,
    pendingDeleteBirthday: BirthdayEntity?,
    onPendingDeleteBirthdayChange: (BirthdayEntity?) -> Unit,
    onConfirmDelete: (BirthdayEntity) -> Unit,
) {
    if (!showDeleteDialog.value) return

    AlertDialog(
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = deleteIcon(),
                contentDescription = "Delete"
            )
        },
        title = { Text("Delete birthday") },
        text = {
            Text(
                if (pendingDeleteBirthday == null) "Confirm deletion?"
                else "Confirm you want to delete ${pendingDeleteBirthday.firstName} ${pendingDeleteBirthday.lastName}?"
            )
        },
        onDismissRequest = {
            showDeleteDialog.value = false
            onPendingDeleteBirthdayChange(null)
        },
        dismissButton = {
            TextButton(onClick = {
                showDeleteDialog.value = false
                onPendingDeleteBirthdayChange(null)
            }) {
                Text("Dismiss", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val b = pendingDeleteBirthday ?: return@TextButton
                    showDeleteDialog.value = false
                    onPendingDeleteBirthdayChange(null)
                    onConfirmDelete(b)
                }
            ) { Text("Delete") }
        }
    )
}

@Composable
fun EditBirthdayPickerDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    birthdays: List<BirthdayEntity>,
    onDismissRequest: () -> Unit,
    onPickEdit: (BirthdayEntity) -> Unit
) {
    if (!showDialog.value) return

    AlertDialog(
        title = { Text(title) },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
            ) {
                items(birthdays.size) { idx ->
                    val b = birthdays[idx]
                    ListItem(
                        headlineContent = { Text("${b.firstName} ${b.lastName}") },
                        supportingContent = {
                            val y = b.yearOfBirth.trim()
                            val formattedDob = formatDobDigits(y)
                            Text(if (y.isBlank()) "Year: —" else "Year: $formattedDob")
                        },
                        trailingContent = {
                            IconButton(onClick = { onPickEdit(b) }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = editIcon(),
                                    contentDescription = "Edit"
                                )
                            }
                        }
                    )
                    if (idx < birthdays.lastIndex)
                        HorizontalDivider()
                }
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) { Text("Close") }
        }
    )
}

@Composable
fun DeleteBirthdayPickerDialog(
    showDialog: Boolean,
    title: String,
    birthdays: List<BirthdayEntity>,
    onDismissRequest: () -> Unit,
    onPickDelete: (BirthdayEntity) -> Unit,
) {
    if (!showDialog) return

    AlertDialog(
        title = { Text(title) },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
            ) {
                items(birthdays.size) { idx ->
                    val b = birthdays[idx]
                    ListItem(
                        headlineContent = { Text("${b.firstName} ${b.lastName}") },
                        supportingContent = {
                            val y = b.yearOfBirth.trim()
                            val formattedDob = formatDobDigits(y)
                            Text(if (y.isBlank()) "Year: —" else "Year: $formattedDob")
                        },
                        trailingContent = {
                            IconButton(onClick = { onPickDelete(b) }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = deleteIcon(),
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    )
                    if (idx < birthdays.lastIndex) HorizontalDivider()
                }
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) { Text("Close") }
        }
    )
}