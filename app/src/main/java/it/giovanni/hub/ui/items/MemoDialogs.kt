package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.data.entity.MemoEntity
import it.giovanni.hub.domain.memo.formatMemoDate

@Composable
fun ViewMemoDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    memos: List<MemoEntity>,
    onEdit: (MemoEntity) -> Unit,
    onDelete: (MemoEntity) -> Unit,
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
                if (memos.isEmpty()) {
                    item { Text("No events.") }
                } else {
                    items(memos.size) { idx ->
                        val memoEntity = memos[idx]
                        ListItem(
                            headlineContent = { Column {
                                Text(memoEntity.memo)
                                Text(
                                    text = formatMemoDate(month = memoEntity.month, day = memoEntity.day) + " - " + memoEntity.time,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }},
                            trailingContent = {
                                Row {
                                    IconButton(onClick = { onEdit(memoEntity) }) {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = editIcon(),
                                            contentDescription = "Edit"
                                        )
                                    }
                                    IconButton(onClick = { onDelete(memoEntity) }) {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = deleteIcon(),
                                            contentDescription = "Delete"
                                        )
                                    }
                                }
                            }
                        )
                        if (idx < memos.lastIndex)
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
fun AddEditMemoDialog(
    title: String,
    icon: Painter,
    confirmButtonText: String,
    showDialog: MutableState<Boolean>,
    memo: MutableState<TextFieldValue>,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    if (!showDialog.value) return

    AlertDialog(
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = "Memo dialog icon"
            )
        },
        title = { Text(title) },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        value = memo.value,
                        onValueChange = { memo.value = it },
                        singleLine = true,
                        placeholder = { Text(stringResource(R.string.memo_label)) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        )
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
            val isButtonEnabled = memo.value.text.isNotBlank()
            TextButton(
                onClick = onConfirmation,
                enabled = isButtonEnabled
            ) { Text(confirmButtonText) }
        }
    )
}

@Composable
fun DeleteMemoDialog(
    showDeleteDialog: MutableState<Boolean>,
    pendingDeleteMemo: MemoEntity?,
    onPendingDeleteMemoChange: (MemoEntity?) -> Unit,
    onConfirmDelete: (MemoEntity) -> Unit,
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
        title = { Text("Delete memo") },
        text = {
            Text(
                if (pendingDeleteMemo == null) "Confirm deletion?"
                else "Confirm you want to delete ${pendingDeleteMemo.memo}?"
            )
        },
        onDismissRequest = {
            showDeleteDialog.value = false
            onPendingDeleteMemoChange(null)
        },
        dismissButton = {
            TextButton(onClick = {
                showDeleteDialog.value = false
                onPendingDeleteMemoChange(null)
            }) {
                Text("Dismiss", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val b = pendingDeleteMemo ?: return@TextButton
                    showDeleteDialog.value = false
                    onPendingDeleteMemoChange(null)
                    onConfirmDelete(b)
                }
            ) { Text("Delete") }
        }
    )
}