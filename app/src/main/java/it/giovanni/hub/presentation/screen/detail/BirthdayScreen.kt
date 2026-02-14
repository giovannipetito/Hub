package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.BirthdayViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.giovanni.hub.data.entity.BirthdayEntity
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
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.giovanni.hub.domain.birthday.rememberDeviceLocale
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import java.time.YearMonth
import java.time.format.TextStyle
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun BirthdayScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    viewModel: BirthdayViewModel = hiltViewModel(),
) {
    val isLoggedIn by mainViewModel.isGoogleLoggedIn.collectAsStateWithLifecycle()

    var searchResult by remember { mutableStateOf("") }
    val textFieldsViewModel: TextFieldsViewModel = viewModel()

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.birthday),
        topics = listOf("Room Database"),
        search = true,
        backup = isLoggedIn,
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
            onView = {
                showViewDialog.value = true
            },
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
            onDismissRequest = {
                showAddDialog.value = false
                resetFields()
            },
            onConfirmation = {
                val date = selectedDate ?: return@AddEditBirthdayDialog
                showAddDialog.value = false
                viewModel.createBirthday(
                    BirthdayEntity(
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        yearOfBirth = yearOfBirth.value.text.trim(),
                        month = date.monthValue,
                        day = date.dayOfMonth
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

@Immutable
private data class MonthModel(
    val year: Int,
    val month: Int, // 1..12
    val title: String,
    val firstDayOffset: Int, // 0..6 offset from weekStartsOn
    val daysInMonth: Int
)

@Composable
fun BirthdayCalendar(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    birthdaysByMonthDay: Map<Int, List<BirthdayEntity>>,
    selectedDate: LocalDate?,
    onDayClick: (LocalDate) -> Unit,
    weekStartsOn: DayOfWeek = DayOfWeek.MONDAY,
    cellSize: Dp = 44.dp,
    cellSpacing: Dp = 6.dp,
) {
    val locale = rememberDeviceLocale()

    val today = remember { LocalDate.now() }
    val year = today.year
    val listState = rememberLazyListState()

    val months = remember(year, locale, weekStartsOn) {
        (1..12).map { m ->
            val ym = YearMonth.of(year, m)
            val title = ym.month.getDisplayName(TextStyle.FULL, locale)
                .replaceFirstChar { it.titlecase(locale) }

            val first = LocalDate.of(year, m, 1).dayOfWeek
            val offset = ((first.value - weekStartsOn.value) + 7) % 7

            MonthModel(
                year = year,
                month = m,
                title = title,
                firstDayOffset = offset,
                daysInMonth = ym.lengthOfMonth()
            )
        }
    }

    // week labels once
    val weekdayLabels = remember(locale, weekStartsOn) {
        val start = weekStartsOn.value // 1..7 (Mon..Sun)
        (0..6).map { i ->
            val v = ((start - 1 + i) % 7) + 1
            DayOfWeek.of(v).getDisplayName(TextStyle.SHORT, locale)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = getExtraContentPadding(
            paddingValues = paddingValues,
            extraPadding = 72.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            count = months.size,
            key = { idx -> "m-${months[idx].year}-${months[idx].month}" }
        ) { idx ->
            val m = months[idx]

            Column(Modifier.padding(horizontal = 12.dp)) {
                Text(
                    text = "${m.title} ${m.year}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 6.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    weekdayLabels.forEach { w ->
                        Text(
                            text = w,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                MonthGridCanvas(
                    month = m,
                    birthdaysByMonthDay = birthdaysByMonthDay,
                    selectedDate = selectedDate,
                    today = today,
                    cellSize = cellSize,
                    cellSpacing = cellSpacing,
                    onDayClick = onDayClick
                )
            }
        }
    }
}

@Composable
private fun MonthGridCanvas(
    month: MonthModel,
    birthdaysByMonthDay: Map<Int, List<BirthdayEntity>>,
    selectedDate: LocalDate?,
    today: LocalDate,
    cellSize: Dp,
    cellSpacing: Dp,
    onDayClick: (LocalDate) -> Unit
) {
    val density = LocalDensity.current
    val cellPx = with(density) { cellSize.toPx() }
    val spacingPx = with(density) { cellSpacing.toPx() }

    val rows = remember(month.year, month.month, month.firstDayOffset, month.daysInMonth) {
        val totalCells = month.firstDayOffset + month.daysInMonth
        ceil(totalCells / 7f).toInt().coerceIn(4, 6)
    }

    val gridWidth = cellPx * 7 + spacingPx * 6
    val gridHeight = cellPx * rows + spacingPx * (rows - 1)

    val cs = MaterialTheme.colorScheme
    val bgBase = cs.surfaceVariant.toArgb()
    val bgSelected = cs.primary.copy(alpha = 0.6f).toArgb()
    val bgToday = cs.primaryContainer.toArgb()
    val bgBirthday = cs.errorContainer.toArgb()
    val textColor = cs.onSurface.toArgb()
    val textDisabled = cs.onSurface.copy(alpha = 0.12f).toArgb()

    val paint = remember {
        Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.CENTER }
    }

    val birthdayKeyForMonth = remember(birthdaysByMonthDay, month.month) {
        BooleanArray(32) { false }.also { arr ->
            birthdaysByMonthDay.keys.forEach { key ->
                val kMonth = key / 100
                val kDay = key % 100
                if (kMonth == month.month && kDay in 1..31) arr[kDay] = true
            }
        }
    }

    val pointer = Modifier.pointerInput(month.year, month.month, month.firstDayOffset, month.daysInMonth, rows) {
        detectTapGestures { pos ->
            val col = floor(pos.x / (cellPx + spacingPx)).toInt()
            val row = floor(pos.y / (cellPx + spacingPx)).toInt()

            if (col !in 0..6 || row !in 0 until rows) return@detectTapGestures

            val inCellX = (pos.x - col * (cellPx + spacingPx)) <= cellPx
            val inCellY = (pos.y - row * (cellPx + spacingPx)) <= cellPx
            if (!inCellX || !inCellY) return@detectTapGestures

            val index = row * 7 + col
            val day = index - month.firstDayOffset + 1
            if (day in 1..month.daysInMonth) {
                onDayClick(LocalDate.of(month.year, month.month, day))
            }
        }
    }

    Canvas(
        modifier = Modifier
            .padding(top = 8.dp)
            .size(
                width = with(density) { gridWidth.toDp() },
                height = with(density) { gridHeight.toDp() }
            )
            .then(pointer)
    ) {
        for (row in 0 until rows) {
            for (col in 0..6) {
                val x = col * (cellPx + spacingPx)
                val y = row * (cellPx + spacingPx)

                val index = row * 7 + col
                val day = index - month.firstDayOffset + 1
                val isRealDay = day in 1..month.daysInMonth

                val isToday =
                    isRealDay && today.year == month.year && today.monthValue == month.month && today.dayOfMonth == day

                val isSelected =
                    isRealDay && selectedDate?.year == month.year &&
                            selectedDate.monthValue == month.month && selectedDate.dayOfMonth == day

                val hasBirthdays = isRealDay && birthdayKeyForMonth[day]

                val bg = when {
                    !isRealDay -> bgBase
                    isSelected -> bgSelected
                    hasBirthdays -> bgBirthday
                    isToday -> bgToday
                    else -> bgBase
                }

                drawRect(
                    color = Color(bg),
                    topLeft = Offset(x, y),
                    size = Size(cellPx, cellPx)
                )

                paint.color = if (isRealDay) textColor else textDisabled
                paint.textSize = cellPx * 0.36f

                val cy = y + cellPx * 0.62f
                drawContext.canvas.nativeCanvas.drawText(
                    if (isRealDay) day.toString() else "",
                    x + cellPx / 2f,
                    cy,
                    paint
                )

                if (isRealDay && hasBirthdays) {
                    drawCircle(
                        color = Color(textColor),
                        radius = cellPx * 0.05f,
                        center = Offset(x + cellPx * 0.82f, y + cellPx * 0.18f)
                    )
                }
            }
        }
    }
}