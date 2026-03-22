package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.data.entity.MemoEntity
import it.giovanni.hub.domain.memo.MemoKind
import it.giovanni.hub.domain.memo.TimeMode
import it.giovanni.hub.domain.memo.TimePickerTarget
import it.giovanni.hub.domain.memo.formatTime

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
                    item { Text("No memos.") }
                } else {
                    items(memos.size) { idx ->
                        val memoEntity = memos[idx]

                        MemoRow(
                            memoEntity = memoEntity,
                            onEdit = onEdit,
                            onDelete = onDelete
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
private fun MemoRow(
    memoEntity: MemoEntity,
    onEdit: (MemoEntity) -> Unit,
    onDelete: (MemoEntity) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = memoEntity.memo,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )

            val time = memoEntity.time
            if (time.isNotBlank() && time != "ALL_DAY") {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onEdit(memoEntity) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = editIcon(),
                    contentDescription = "Edit"
                )
            }

            IconButton(
                onClick = { onDelete(memoEntity) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = deleteIcon(),
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMemoDialog(
    birthdayTitle: String,
    eventTitle: String,
    icon: Painter,
    confirmButtonText: String,
    showDialog: MutableState<Boolean>,
    input: MutableState<TextFieldValue>,
    onDismissRequest: () -> Unit,
    onConfirmation: (isBirthday: Boolean, time: String) -> Unit,
) {
    if (!showDialog.value) return

    var memoKind by rememberSaveable { mutableStateOf(MemoKind.BIRTHDAY) }
    var timeMode by rememberSaveable { mutableStateOf(TimeMode.ALL_DAY) }

    var singleHour by rememberSaveable { mutableIntStateOf(12) }
    var singleMinute by rememberSaveable { mutableIntStateOf(0) }

    var startHour by rememberSaveable { mutableIntStateOf(12) }
    var startMinute by rememberSaveable { mutableIntStateOf(0) }

    var endHour by rememberSaveable { mutableIntStateOf(13) }
    var endMinute by rememberSaveable { mutableIntStateOf(0) }

    var openPicker by rememberSaveable { mutableStateOf<TimePickerTarget?>(null) }

    fun buildSavedTime(): String {
        return when (memoKind) {
            MemoKind.BIRTHDAY -> ""
            MemoKind.GENERIC_EVENT -> {
                when (timeMode) {
                    TimeMode.ALL_DAY -> "ALL_DAY"
                    TimeMode.SINGLE_TIME -> formatTime(singleHour, singleMinute)
                    TimeMode.TIME_RANGE -> {
                        "${formatTime(startHour, startMinute)}-${formatTime(endHour, endMinute)}"
                    }
                }
            }
        }
    }

    fun isRangeValid(): Boolean {
        val start = startHour * 60 + startMinute
        val end = endHour * 60 + endMinute
        return end > start
    }

    fun isFormValid(): Boolean {
        if (input.value.text.isBlank()) return false

        return when (memoKind) {
            MemoKind.BIRTHDAY -> true
            MemoKind.GENERIC_EVENT -> when (timeMode) {
                TimeMode.ALL_DAY -> true
                TimeMode.SINGLE_TIME -> true
                TimeMode.TIME_RANGE -> isRangeValid()
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = icon,
                contentDescription = "Memo dialog icon"
            )
        },
        title = { Text(if (memoKind == MemoKind.BIRTHDAY) birthdayTitle else eventTitle) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    value = input.value,
                    onValueChange = { input.value = it },
                    singleLine = true,
                    placeholder = {
                        if (memoKind == MemoKind.BIRTHDAY)
                            Text(stringResource(R.string.memo_birthday_label))
                        else
                            Text(stringResource(R.string.memo_event_label))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = memoKind == MemoKind.BIRTHDAY,
                        onClick = { memoKind = MemoKind.BIRTHDAY },
                        label = { Text("Birthday") }
                    )
                    FilterChip(
                        selected = memoKind == MemoKind.GENERIC_EVENT,
                        onClick = { memoKind = MemoKind.GENERIC_EVENT },
                        label = { Text("Event") }
                    )
                }

                if (memoKind == MemoKind.GENERIC_EVENT) {

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = timeMode == TimeMode.ALL_DAY,
                            onClick = { timeMode = TimeMode.ALL_DAY },
                            label = { Text("All day") }
                        )
                        FilterChip(
                            selected = timeMode == TimeMode.SINGLE_TIME,
                            onClick = { timeMode = TimeMode.SINGLE_TIME },
                            label = { Text("At time") }
                        )
                        FilterChip(
                            selected = timeMode == TimeMode.TIME_RANGE,
                            onClick = { timeMode = TimeMode.TIME_RANGE },
                            label = { Text("Range") }
                        )
                    }

                    when (timeMode) {
                        TimeMode.ALL_DAY -> {
                            /*
                            Text(
                                text = "This event will be saved as all day.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            */
                        }

                        TimeMode.SINGLE_TIME -> {
                            OutlinedButton(
                                onClick = { openPicker = TimePickerTarget.SINGLE }
                            ) {
                                Text("Event time: ${formatTime(singleHour, singleMinute)}")
                            }
                        }

                        TimeMode.TIME_RANGE -> {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                    OutlinedButton(
                                        onClick = { openPicker = TimePickerTarget.START }
                                    ) {
                                        Text("Start: ${formatTime(startHour, startMinute)}")
                                    }

                                    OutlinedButton(
                                        onClick = { openPicker = TimePickerTarget.END }
                                    ) {
                                        Text("End: ${formatTime(endHour, endMinute)}")
                                    }
                                }

                                if (!isRangeValid()) {
                                    Text(
                                        text = "End time must be later than start time.",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Dismiss", color = MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(
                        memoKind == MemoKind.BIRTHDAY,
                        buildSavedTime()
                    )
                },
                enabled = isFormValid()
            ) {
                Text(confirmButtonText)
            }
        }
    )

    when (openPicker) {
        TimePickerTarget.SINGLE -> {
            AppTimePickerDialog(
                title = "Select time",
                initialHour = singleHour,
                initialMinute = singleMinute,
                onDismiss = { openPicker = null },
                onConfirm = { hour, minute ->
                    singleHour = hour
                    singleMinute = minute
                    openPicker = null
                }
            )
        }

        TimePickerTarget.START -> {
            AppTimePickerDialog(
                title = "Select start time",
                initialHour = startHour,
                initialMinute = startMinute,
                onDismiss = { openPicker = null },
                onConfirm = { hour, minute ->
                    startHour = hour
                    startMinute = minute
                    openPicker = null
                }
            )
        }

        TimePickerTarget.END -> {
            AppTimePickerDialog(
                title = "Select end time",
                initialHour = endHour,
                initialMinute = endMinute,
                onDismiss = { openPicker = null },
                onConfirm = { hour, minute ->
                    endHour = hour
                    endMinute = minute
                    openPicker = null
                }
            )
        }

        null -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(
    title: String,
    initialHour: Int = 12,
    initialMinute: Int = 0,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            TimePicker(
                state = timePickerState
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
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