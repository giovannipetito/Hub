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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.giovanni.hub.data.entity.BirthdayEntity
import it.giovanni.hub.domain.birthday.buildYearEntries
import it.giovanni.hub.presentation.viewmodel.TextFieldsViewModel
import it.giovanni.hub.ui.items.AddEditBirthdayDialog
import it.giovanni.hub.ui.items.DeleteBirthdayPickerDialog
import it.giovanni.hub.ui.items.EditBirthdayPickerDialog
import it.giovanni.hub.ui.items.ViewBirthdayDialog
import it.giovanni.hub.ui.items.DeleteBirthdayDialog
import it.giovanni.hub.ui.items.ExpandableBirthdayFAB
import it.giovanni.hub.ui.items.addIcon
import it.giovanni.hub.ui.items.editIcon
import it.giovanni.hub.utils.Globals.getExtraContentPadding
import it.giovanni.hub.utils.SearchWidgetState
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

@Composable
fun BirthdayScreen(
    navController: NavController,
    viewModel: BirthdayViewModel = hiltViewModel()
) {
    var searchResult by remember { mutableStateOf("") }
    val textFieldsViewModel: TextFieldsViewModel = viewModel()

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.birthday),
        topics = listOf("Room Database"),
        search = true,
        placeholder = "Search birthday by name...",
        onSearchResult = { result -> searchResult = result },
        onCloseResult = { searchResult = "" }
    ) { paddingValues ->

        val allBirthdays: List<BirthdayEntity> by viewModel.birthdays.collectAsState()
        val searchedBirthdays: List<BirthdayEntity> by viewModel.searchResults.collectAsState()

        val showSearchDialog = remember { mutableStateOf(false) }
        var lastSearchText by remember { mutableStateOf("") }

        LaunchedEffect(searchResult) {
            val q = searchResult.trim()
            lastSearchText = q

            if (q.isBlank()) {
                showSearchDialog.value = false
                viewModel.clearSearch()
            } else {
                viewModel.searchBirthdays(q)
                showSearchDialog.value = true
            }
        }

        // FAB
        var fabExpanded by remember { mutableStateOf(false) }

        // Selected cell/date
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

        // Dialogs
        val showViewDialog = remember { mutableStateOf(false) }

        val showAddDialog = remember { mutableStateOf(false) }

        val showEditDialog = remember { mutableStateOf(false) }
        val showEditPickerDialog = remember { mutableStateOf(false) }
        var editingBirthday by remember { mutableStateOf<BirthdayEntity?>(null) }

        val showDeleteDialog = remember { mutableStateOf(false) }
        val showDeletePickerDialog = rememberSaveable { mutableStateOf(false) }
        var deletingBirthday by remember { mutableStateOf<BirthdayEntity?>(null) }

        var deleteDialogDayKey by remember { mutableStateOf<Int?>(null) }

        // Fields
        val firstName = remember { mutableStateOf(TextFieldValue("")) }
        val lastName = remember { mutableStateOf(TextFieldValue("")) }
        val yearOfBirth = remember { mutableStateOf(TextFieldValue("")) }

        fun resetFields() {
            firstName.value = TextFieldValue("")
            lastName.value = TextFieldValue("")
            yearOfBirth.value = TextFieldValue("")
        }

        val birthdaysByMonthDay: Map<Int, List<BirthdayEntity>> = remember(allBirthdays) {
            allBirthdays.groupBy { it.month * 100 + it.day }
        }

        val selectedBirthdays: List<BirthdayEntity> = remember(selectedDate, birthdaysByMonthDay) {
            val date = selectedDate ?: return@remember emptyList()
            birthdaysByMonthDay[date.monthValue * 100 + date.dayOfMonth].orEmpty()
        }

        val hasBirthdaysInSelection = selectedBirthdays.isNotEmpty()
        val hasSelection = selectedDate != null

        LaunchedEffect(
            showDeleteDialog.value,
            showDeletePickerDialog.value,
            deleteDialogDayKey,
            birthdaysByMonthDay
        ) {
            if (!showDeleteDialog.value && showDeletePickerDialog.value) {
                val key = deleteDialogDayKey
                val isDayEmpty = key != null && birthdaysByMonthDay[key].orEmpty().isEmpty()

                if (isDayEmpty) {
                    showDeletePickerDialog.value = false
                    deleteDialogDayKey = null // cleanup
                }
            }
        }

        // --- Calendar ---
        BirthdayCalendar(
            modifier = Modifier.fillMaxSize(),
            paddingValues = paddingValues,
            birthdaysByMonthDay = birthdaysByMonthDay,
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
                showAddDialog.value = true
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
                    deletingBirthday = picked
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

        // Search Dialog
        ViewBirthdayDialog(
            showDialog = showSearchDialog,
            title = if (lastSearchText.isBlank()) "Search results"
            else "Birthdays matching \"$lastSearchText\"",
            birthdays = searchedBirthdays,
            onDismissRequest = {
                showSearchDialog.value = false

                searchResult = ""
                textFieldsViewModel.updateSearchTextState("")
                textFieldsViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
            }
        )

        // View Dialog
        ViewBirthdayDialog(
            showDialog = showViewDialog,
            title = "Birthdays in this day",
            birthdays = selectedBirthdays,
            onDismissRequest = { showViewDialog.value = false }
        )

        // Add Dialog
        AddEditBirthdayDialog(
            title = "Add Birthday",
            icon = addIcon(),
            confirmButtonText = "Create",
            showDialog = showAddDialog,
            firstName = firstName,
            lastName = lastName,
            yearOfBirth = yearOfBirth,
            onDismissRequest = {
                showAddDialog.value = false
                resetFields()
            },
            onConfirmation = {
                val d = selectedDate ?: return@AddEditBirthdayDialog
                showAddDialog.value = false
                viewModel.createBirthday(
                    BirthdayEntity(
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        yearOfBirth = yearOfBirth.value.text.trim(),
                        month = d.monthValue,
                        day = d.dayOfMonth
                    )
                )
                resetFields()
            }
        )

        // Edit Dialog
        AddEditBirthdayDialog(
            title = "Edit Birthday",
            icon = editIcon(),
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
                val old = editingBirthday ?: return@AddEditBirthdayDialog
                val d = selectedDate ?: return@AddEditBirthdayDialog

                showEditDialog.value = false
                viewModel.updateBirthday(
                    old.copy(
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        yearOfBirth = yearOfBirth.value.text.trim(),
                        month = d.monthValue,
                        day = d.dayOfMonth
                    )
                )
                resetFields()
            }
        )

        // Delete Dialog
        DeleteBirthdayDialog(
            showDeleteDialog = showDeleteDialog,
            pendingDeleteBirthday = deletingBirthday,
            onPendingDeleteBirthdayChange = { deletingBirthday = it },
            onConfirmDelete = { b -> viewModel.deleteBirthday(b) }
        )

        // Edit Picker Dialog
        EditBirthdayPickerDialog(
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

        // Delete Picker Dialog
        DeleteBirthdayPickerDialog(
            showDialog = showDeletePickerDialog.value,
            title = "Select birthday to delete",
            birthdays = selectedBirthdays,
            onDismissRequest = { showDeletePickerDialog.value = false },
            onPickDelete = { picked ->
                deletingBirthday = picked
                deleteDialogDayKey = picked.month * 100 + picked.day
                showDeleteDialog.value = true
            }
        )
    }
}

// Calendar types

sealed interface CalendarEntry {
    data class MonthHeader(val year: Int, val month: Int, val title: String) : CalendarEntry
    data class WeekdayLabel(val text: String, val key: String) : CalendarEntry
    data class Day(val year: Int, val month: Int, val day: Int?, val key: String) : CalendarEntry
    data class Spacer(val key: String) : CalendarEntry
}

@Composable
fun BirthdayCalendar(
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
        contentPadding = getExtraContentPadding(
            paddingValues = paddingValues,
            extraPadding = 72.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(cellSpacing),
        verticalArrangement = Arrangement.spacedBy(cellSpacing),
    ) {
        items(
            count = entries.size,
            key = { idx ->
                when (val e = entries[idx]) {
                    is CalendarEntry.MonthHeader -> "mh-${e.year}-${e.month}"
                    is CalendarEntry.WeekdayLabel -> e.key
                    is CalendarEntry.Day -> e.key
                    is CalendarEntry.Spacer -> e.key
                }
            },
            span = { idx ->
                when (entries[idx]) {
                    is CalendarEntry.MonthHeader, is CalendarEntry.Spacer -> GridItemSpan(7)
                    else -> GridItemSpan(1)
                }
            },
            contentType = { idx ->
                when (entries[idx]) {
                    is CalendarEntry.MonthHeader -> "monthHeader"
                    is CalendarEntry.WeekdayLabel -> "weekday"
                    is CalendarEntry.Day -> "day"
                    is CalendarEntry.Spacer -> "spacer"
                }
            }
        ) { idx ->
            when (val entry = entries[idx]) {
                is CalendarEntry.MonthHeader -> {
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

                is CalendarEntry.WeekdayLabel -> {
                    Text(
                        text = entry.text,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth().height(18.dp),
                        textAlign = TextAlign.Center
                    )
                }

                is CalendarEntry.Day -> {
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

                is CalendarEntry.Spacer -> Spacer(modifier = Modifier.height(2.dp))
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

@Preview(showBackground = true)
@Composable
fun BirthdayScreenScreenPreview() {
    BirthdayScreen(navController = rememberNavController())
}