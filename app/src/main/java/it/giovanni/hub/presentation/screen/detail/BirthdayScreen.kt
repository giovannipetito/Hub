package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.BirthdayViewModel
import it.giovanni.hub.ui.items.deleteIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import it.giovanni.hub.data.entity.BirthdayEntity
import it.giovanni.hub.ui.items.BirthdayTextFieldsDialog
import it.giovanni.hub.ui.items.BirthdaysDeletePickerDialog
import it.giovanni.hub.ui.items.BirthdaysEditPickerDialog
import it.giovanni.hub.ui.items.BirthdaysForDayDialog
import it.giovanni.hub.ui.items.ExpandableBirthdayFAB
import it.giovanni.hub.utils.Globals.getContentPadding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun BirthdayScreen(
    navController: NavController,
    viewModel: BirthdayViewModel = hiltViewModel()
) {
    var searchResult by remember { mutableStateOf("") }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.birthday),
        topics = listOf("Room Database"),
        search = true,
        placeholder = "Search birthday by name...",
        onSearchResult = { result -> searchResult = result }
    ) { paddingValues ->

        val birthdays: List<BirthdayEntity> by viewModel.birthdays.collectAsState()

        // When search changes -> re-read list with filtering
        LaunchedEffect(searchResult) {
            viewModel.readBirthdays(search = searchResult.trim())
        }

        // Selected cell (date)
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
        var fabExpanded by remember { mutableStateOf(false) }

        // dialogs
        val showCreateDialog = remember { mutableStateOf(false) }

        val showViewDialog = remember { mutableStateOf(false) }

        val showEditDialog = remember { mutableStateOf(false) }
        val showEditPickerDialog = remember { mutableStateOf(false) }
        var editingBirthday by remember { mutableStateOf<BirthdayEntity?>(null) }

        val showDeleteDialog = remember { mutableStateOf(false) }
        val showDeletePickerDialog = remember { mutableStateOf(false) }
        var pendingDeleteBirthday by remember { mutableStateOf<BirthdayEntity?>(null) }

        var deleteDialogDayKey by remember { mutableStateOf<Int?>(null) }

        // fields
        val firstName = remember { mutableStateOf(TextFieldValue("")) }
        val lastName = remember { mutableStateOf(TextFieldValue("")) }
        val yearOfBirth = remember { mutableStateOf(TextFieldValue("")) }

        fun resetFields() {
            firstName.value = TextFieldValue("")
            lastName.value = TextFieldValue("")
            yearOfBirth.value = TextFieldValue("")
        }

        val byMonthDay: Map<Int, List<BirthdayEntity>> = remember(birthdays) {
            birthdays.groupBy { it.month * 100 + it.day }
        }

        val selectedBirthdays: List<BirthdayEntity> = remember(selectedDate, byMonthDay) {
            val d = selectedDate ?: return@remember emptyList()
            byMonthDay[d.monthValue * 100 + d.dayOfMonth].orEmpty()
        }

        val hasBirthdaysInSelection = selectedBirthdays.isNotEmpty()
        val hasSelection = selectedDate != null

        LaunchedEffect(
            showDeleteDialog.value,
            showDeletePickerDialog.value,
            deleteDialogDayKey,
            byMonthDay
        ) {
            if (!showDeleteDialog.value && showDeletePickerDialog.value) {
                val key = deleteDialogDayKey
                val isDayEmpty = key != null && byMonthDay[key].orEmpty().isEmpty()

                if (isDayEmpty) {
                    showDeletePickerDialog.value = false
                    deleteDialogDayKey = null // cleanup
                }
            }
        }

        // --- Calendar ---
        CurrentYearCalendarSmoothSpans(
            modifier = Modifier.fillMaxSize(),
            paddingValues = paddingValues,
            birthdaysByMonthDay = byMonthDay,
            selectedDate = selectedDate,
            onDayClick = { clicked ->
                if (selectedDate == clicked) {
                    selectedDate = null
                    fabExpanded = false
                } else {
                    selectedDate = clicked
                    fabExpanded = true
                }
            }
        )

        // --- FAB ---
        ExpandableBirthdayFAB(
            paddingValues = paddingValues,
            expanded = fabExpanded && hasSelection,
            hasSelection = hasSelection,
            hasBirthdaysInSelection = hasBirthdaysInSelection,
            canEditSingleBirthday = hasBirthdaysInSelection,
            onExpandedChange = { fabExpanded = it },
            onAdd = {
                resetFields()
                showCreateDialog.value = true
            },
            onEdit = {
                if (selectedBirthdays.isEmpty())
                    return@ExpandableBirthdayFAB

                if (selectedBirthdays.size == 1) {
                    val b = selectedBirthdays.first()
                    editingBirthday = b
                    firstName.value = TextFieldValue(b.firstName)
                    lastName.value = TextFieldValue(b.lastName)
                    yearOfBirth.value = TextFieldValue(b.yearOfBirth)
                    showEditDialog.value = true
                } else {
                    showEditPickerDialog.value = true
                }
            },
            onDelete = {
                if (selectedBirthdays.isEmpty())
                    return@ExpandableBirthdayFAB
                if (selectedBirthdays.size == 1) {
                    val picked = selectedBirthdays.first()
                    pendingDeleteBirthday = picked
                    deleteDialogDayKey = picked.month * 100 + picked.day
                    showDeleteDialog.value = true
                } else {
                    showDeletePickerDialog.value = true
                }
            },
            onView = {
                showViewDialog.value = true
            }
        )

        // --- Create dialog ---
        BirthdayTextFieldsDialog(
            title = "Add Birthday",
            confirmButtonText = "Create",
            showDialog = showCreateDialog,
            firstName = firstName,
            lastName = lastName,
            yearOfBirth = yearOfBirth,
            onDismissRequest = {
                showCreateDialog.value = false
                resetFields()
            },
            onConfirmation = {
                val d = selectedDate ?: return@BirthdayTextFieldsDialog
                showCreateDialog.value = false
                viewModel.createBirthday(
                    BirthdayEntity(
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        yearOfBirth = yearOfBirth.value.text.trim(), // can be ""
                        month = d.monthValue,
                        day = d.dayOfMonth
                    )
                )
                resetFields()
            }
        )

        // --- Edit dialog (edits the single birthday in selection) ---
        BirthdayTextFieldsDialog(
            title = "Edit Birthday",
            confirmButtonText = "Update",
            showDialog = showEditDialog,
            firstName = firstName,
            lastName = lastName,
            yearOfBirth = yearOfBirth,
            onDismissRequest = {
                showEditDialog.value = false
                resetFields()
            },
            onConfirmation = {
                val old = editingBirthday ?: return@BirthdayTextFieldsDialog
                val d = selectedDate ?: return@BirthdayTextFieldsDialog

                showEditDialog.value = false
                viewModel.updateBirthday(
                    old.copy(
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        yearOfBirth = yearOfBirth.value.text.trim(), // can be ""
                        month = d.monthValue,
                        day = d.dayOfMonth
                    )
                )
                resetFields()
            }
        )

        BirthdaysEditPickerDialog(
            showDialog = showEditPickerDialog,
            title = "Select birthday to edit",
            birthdays = selectedBirthdays,
            onDismissRequest = { showEditPickerDialog.value = false },
            onPickEdit = { picked ->
                showEditPickerDialog.value = false
                editingBirthday = picked

                firstName.value = TextFieldValue(picked.firstName)
                lastName.value = TextFieldValue(picked.lastName)
                yearOfBirth.value = TextFieldValue(picked.yearOfBirth)

                showEditDialog.value = true
            }
        )

        BirthdaysDeletePickerDialog(
            showDialog = showDeletePickerDialog,
            title = "Select birthday to delete",
            birthdays = selectedBirthdays,
            onDismissRequest = { showDeletePickerDialog.value = false },
            onPickDelete = { picked ->
                pendingDeleteBirthday = picked
                deleteDialogDayKey = picked.month * 100 + picked.day
                showDeleteDialog.value = true
            }
        )

        // --- Delete-for-day dialog (deletes ALL birthdays in that cell) ---
        if (showDeleteDialog.value) {
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
                    val b = pendingDeleteBirthday
                    Text(
                        if (b == null) "Confirm deletion?"
                        else "Confirm you want to delete ${b.firstName} ${b.lastName}?"
                    )
                },
                onDismissRequest = {
                    showDeleteDialog.value = false
                    pendingDeleteBirthday = null
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteDialog.value = false
                        pendingDeleteBirthday = null
                    }) {
                        Text("Dismiss", color = MaterialTheme.colorScheme.error)
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val b = pendingDeleteBirthday ?: return@TextButton
                            showDeleteDialog.value = false
                            pendingDeleteBirthday = null
                            viewModel.deleteBirthday(b)
                        }
                    ) { Text("Delete") }
                }
            )
        }

        // --- View dialog with LazyColumn ---
        BirthdaysForDayDialog(
            showDialog = showViewDialog,
            title = "Birthdays in this day",
            birthdays = selectedBirthdays,
            onDismissRequest = { showViewDialog.value = false }
        )
    }
}

// ---------------- Calendar types ----------------

sealed interface CalEntry {
    data class MonthHeader(val year: Int, val month: Int, val title: String) : CalEntry
    data class WeekdayLabel(val text: String, val key: String) : CalEntry
    data class Day(val year: Int, val month: Int, val day: Int?, val key: String) : CalEntry
    data class Spacer(val key: String) : CalEntry
}

@Composable
fun CurrentYearCalendarSmoothSpans(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    birthdaysByMonthDay: Map<Int, List<BirthdayEntity>>,
    selectedDate: LocalDate?,
    onDayClick: (LocalDate) -> Unit,
    locale: Locale = Locale.ITALIAN,
    weekStartsOn: DayOfWeek = DayOfWeek.MONDAY,
    cellSize: Dp = 44.dp,
    cellSpacing: Dp = 6.dp,
) {
    val today = remember { LocalDate.now() }
    val year = today.year
    val state = rememberLazyGridState()

    val entries = remember(year, locale, weekStartsOn) {
        buildYearEntries(year, locale, weekStartsOn)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        state = state,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        contentPadding = getContentPadding(paddingValues = paddingValues),
        horizontalArrangement = Arrangement.spacedBy(cellSpacing),
        verticalArrangement = Arrangement.spacedBy(cellSpacing),
    ) {
        items(
            count = entries.size,
            key = { idx ->
                when (val e = entries[idx]) {
                    is CalEntry.MonthHeader -> "mh-${e.year}-${e.month}"
                    is CalEntry.WeekdayLabel -> e.key
                    is CalEntry.Day -> e.key
                    is CalEntry.Spacer -> e.key
                }
            },
            span = { idx ->
                when (entries[idx]) {
                    is CalEntry.MonthHeader, is CalEntry.Spacer -> GridItemSpan(7)
                    else -> GridItemSpan(1)
                }
            },
            contentType = { idx ->
                when (entries[idx]) {
                    is CalEntry.MonthHeader -> "monthHeader"
                    is CalEntry.WeekdayLabel -> "weekday"
                    is CalEntry.Day -> "day"
                    is CalEntry.Spacer -> "spacer"
                }
            }
        ) { idx ->
            when (val entry = entries[idx]) {
                is CalEntry.MonthHeader -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Transparent,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        Text(
                            text = entry.title,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        )
                    }
                }

                is CalEntry.WeekdayLabel -> {
                    Text(
                        text = entry.text,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth().height(18.dp),
                        textAlign = TextAlign.Center
                    )
                }

                is CalEntry.Day -> {
                    val isRealDay = entry.day != null
                    val hasBirthdays =
                        isRealDay && birthdaysByMonthDay.containsKey(entry.month * 100 + entry.day)

                    val date = if (isRealDay) LocalDate.of(entry.year, entry.month, entry.day) else null
                    val isSelected = date != null && selectedDate != null && date == selectedDate

                    DayCell(
                        day = entry.day,
                        isToday = (entry.day != null &&
                                today.year == entry.year &&
                                today.monthValue == entry.month &&
                                today.dayOfMonth == entry.day),
                        hasBirthdays = hasBirthdays,
                        isSelected = isSelected,
                        size = cellSize,
                        onClick = {
                            if (date != null) onDayClick(date)
                        }
                    )
                }

                is CalEntry.Spacer -> Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Composable
private fun DayCell(
    day: Int?,
    isToday: Boolean,
    hasBirthdays: Boolean,
    isSelected: Boolean,
    size: Dp,
    onClick: () -> Unit,
) {
    val selectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    val todayColor = MaterialTheme.colorScheme.primaryContainer
    val red = MaterialTheme.colorScheme.errorContainer
    val base = MaterialTheme.colorScheme.surfaceVariant

    val bg = when {
        day == null -> base
        isSelected -> selectedColor
        hasBirthdays -> red
        isToday -> todayColor
        else -> base
    }

    val textColor =
        if (day == null) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .size(size)
            .background(bg, shape = MaterialTheme.shapes.small)
            .clickable(enabled = day != null) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day?.toString() ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

private fun buildYearEntries(
    year: Int,
    locale: Locale,
    weekStartsOn: DayOfWeek
): List<CalEntry> {
    fun monthTitle(month: Int): String {
        val name = YearMonth.of(year, month).month.getDisplayName(TextStyle.FULL, locale)
        return name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    }

    fun orderedWeekdays(): List<DayOfWeek> =
        (0..6).map { weekStartsOn.plus(it.toLong()) }

    fun dowIndex(d: DayOfWeek): Int {
        val raw = (d.value - weekStartsOn.value) % 7
        return if (raw < 0) raw + 7 else raw
    }

    val out = ArrayList<CalEntry>(600)

    for (month in 1..12) {
        out.add(CalEntry.MonthHeader(year, month, monthTitle(month)))

        val wds = orderedWeekdays()
        wds.forEach { dow ->
            out.add(
                CalEntry.WeekdayLabel(
                    text = dow.getDisplayName(TextStyle.NARROW, locale),
                    key = "wd-$year-$month-${dow.value}"
                )
            )
        }

        val ym = YearMonth.of(year, month)
        val firstDow = ym.atDay(1).dayOfWeek
        val leading = dowIndex(firstDow)
        val daysInMonth = ym.lengthOfMonth()
        val total = leading + daysInMonth
        val trailing = (7 - (total % 7)) % 7

        repeat(leading) { i ->
            out.add(CalEntry.Day(year, month, null, key = "d-$year-$month-lead-$i"))
        }

        for (d in 1..daysInMonth) {
            out.add(CalEntry.Day(year, month, d, key = "d-$year-$month-$d"))
        }

        repeat(trailing) { i ->
            out.add(CalEntry.Day(year, month, null, key = "d-$year-$month-trail-$i"))
        }

        out.add(CalEntry.Spacer(key = "sp-$year-$month"))
    }

    return out
}

@Preview(showBackground = true)
@Composable
fun BirthdayScreenScreenPreview() {
    BirthdayScreen(navController = rememberNavController())
}