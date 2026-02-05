package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
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
import it.giovanni.hub.data.entity.UserEntity
import it.giovanni.hub.presentation.viewmodel.BirthdayViewModel
import it.giovanni.hub.ui.items.ExpandableRoomFAB
import it.giovanni.hub.ui.items.HubAlertDialog
import it.giovanni.hub.ui.items.TextFieldsDialog
import it.giovanni.hub.ui.items.addIcon
import it.giovanni.hub.ui.items.deleteIcon
import it.giovanni.hub.ui.items.deleteUserIcon
import it.giovanni.hub.ui.items.editIcon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
    var searchResult: String by remember { mutableStateOf("") }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.birthday),
        topics = listOf("Room Database"),
        search = true,
        placeholder = "Search user by Id...",
        onSearchResult = { result ->
            searchResult = result
        }
    ) { paddingValues ->

        val users: List<UserEntity> by viewModel.users.collectAsState()

        val showCreateUserDialog = remember { mutableStateOf(false) }
        val showUpdateUserDialog = remember { mutableStateOf(false) }
        val showDeleteUserDialog = remember { mutableStateOf(false) }
        val showDeleteUsersDialog = remember { mutableStateOf(false) }

        val id: MutableState<Int> = remember { mutableIntStateOf(0) }
        val firstName: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
        val lastName: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
        val age: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }

        /*
        LaunchedEffect(Unit) {
            viewModel.readUsers()
        }
        */

        fun canParseToInt(input: String): Boolean {
            return input.toIntOrNull() != null
        }

        if (canParseToInt(input = searchResult)) {
            viewModel.readUserById(id = searchResult.toInt())
        }

        fun resetUserInfo() {
            id.value = 0
            firstName.value = TextFieldValue("")
            lastName.value = TextFieldValue("")
            age.value = TextFieldValue("")
        }

        fun validateUserInfo(user: UserEntity) {
            id.value = user.id
            firstName.value = TextFieldValue(user.firstName)
            lastName.value = TextFieldValue(user.lastName)
            age.value = TextFieldValue(user.age)
        }

        CurrentYearCalendarSmoothSpans(modifier = Modifier.fillMaxSize())

        ExpandableRoomFAB(
            paddingValues = paddingValues,
            users = users,
            onShowCreateUserDialog = {
                showCreateUserDialog.value = it
            },
            onShowDeleteUsersDialog = {
                showDeleteUsersDialog.value = it
            },
            onResetUserInfo = {
                resetUserInfo()
            }
        )

        TextFieldsDialog(
            icon = addIcon(),
            title = "Create User",
            text = "Confirm you want to create this user?",
            firstName = firstName,
            lastName = lastName,
            age = age,
            dismissButtonText = "Dismiss",
            confirmButtonText = "Create",
            showDialog = showCreateUserDialog,
            onDismissRequest = {
                showCreateUserDialog.value = false
                resetUserInfo()
            },
            onConfirmation = {
                showCreateUserDialog.value = false
                viewModel.createUser(
                    userEntity = UserEntity(
                        id = id.value,
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        age = age.value.text
                    )
                )
            }
        )

        TextFieldsDialog(
            icon = editIcon(),
            title = "Update User",
            text = "Confirm you want to update this user?",
            firstName = firstName,
            lastName = lastName,
            age = age,
            dismissButtonText = "Dismiss",
            confirmButtonText = "Update",
            showDialog = showUpdateUserDialog,
            onDismissRequest = {
                showUpdateUserDialog.value = false
                resetUserInfo()
            },
            onConfirmation = {
                showUpdateUserDialog.value = false
                viewModel.updateUser(
                    userEntity = UserEntity(
                        id = id.value,
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        age = age.value.text
                    )
                )
            }
        )

        HubAlertDialog(
            icon = deleteUserIcon(),
            title = "Delete User",
            text = "Confirm you want to delete this user?",
            dismissButtonText = "Dismiss",
            confirmButtonText = "Delete",
            showDialog = showDeleteUserDialog,
            onDismissRequest = {
                showDeleteUserDialog.value = false
                resetUserInfo()
            },
            onConfirmation = {
                showDeleteUserDialog.value = false
                viewModel.deleteUser(
                    userEntity = UserEntity(
                        id = id.value,
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        age = age.value.text
                    )
                )
            }
        )

        HubAlertDialog(
            icon = deleteIcon(),
            title = "Delete Users",
            text = "Confirm you want to delete all the users?",
            dismissButtonText = "Dismiss",
            confirmButtonText = "Delete",
            showDialog = showDeleteUsersDialog,
            onDismissRequest = {
                showDeleteUsersDialog.value = false
                resetUserInfo()
            },
            onConfirmation = {
                showDeleteUsersDialog.value = false
                viewModel.deleteUsers()
            }
        )
    }
}

sealed interface CalEntry {
    data class MonthHeader(val year: Int, val month: Int, val title: String) : CalEntry
    data class WeekdayLabel(val text: String, val key: String) : CalEntry
    data class Day(val year: Int, val month: Int, val day: Int?, val key: String) : CalEntry
    data class Spacer(val key: String) : CalEntry
}

/**
 * Versione con span corretto usando itemsIndexed (necessario per gestire span su lista).
 */
@Composable
fun CurrentYearCalendarSmoothSpans(
    modifier: Modifier = Modifier,
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
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
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
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        Text(
                            text = entry.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        )
                    }
                }

                is CalEntry.WeekdayLabel -> {
                    Text(
                        text = entry.text,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(18.dp),
                        textAlign = TextAlign.Center
                    )
                }

                is CalEntry.Day -> {
                    DayCell(
                        day = entry.day,
                        isToday = (entry.day != null &&
                                today.year == entry.year &&
                                today.monthValue == entry.month &&
                                today.dayOfMonth == entry.day),
                        size = cellSize
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
    size: Dp,
) {
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.primaryContainer
    val textColor =
        if (day == null) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .size(size)
            .background(if (isToday) highlight else base, shape = MaterialTheme.shapes.small),
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

        // weekday labels row
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

        // leading blanks
        repeat(leading) { i ->
            out.add(CalEntry.Day(year, month, null, key = "d-$year-$month-lead-$i"))
        }
        // days
        for (d in 1..daysInMonth) {
            out.add(CalEntry.Day(year, month, d, key = "d-$year-$month-$d"))
        }
        // trailing blanks
        repeat(trailing) { i ->
            out.add(CalEntry.Day(year, month, null, key = "d-$year-$month-trail-$i"))
        }

        // spacer full width between months
        out.add(CalEntry.Spacer(key = "sp-$year-$month"))
    }

    return out
}

@Preview(showBackground = true)
@Composable
fun BirthdayScreenScreenPreview() {
    BirthdayScreen(navController = rememberNavController())
}