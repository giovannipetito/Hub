package it.giovanni.hub.presentation.screen.detail

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.BirthdayViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import it.giovanni.hub.data.entity.MemoEntity
import it.giovanni.hub.presentation.viewmodel.TextFieldsViewModel
import it.giovanni.hub.ui.items.AddEditBirthdayDialog
import it.giovanni.hub.ui.items.ViewBirthdayDialog
import it.giovanni.hub.ui.items.DeleteBirthdayDialog
import it.giovanni.hub.ui.items.ExpandableBirthdayFAB
import it.giovanni.hub.ui.items.addIcon
import it.giovanni.hub.ui.items.editIcon
import it.giovanni.hub.utils.SearchWidgetState
import java.time.LocalDate
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.giovanni.hub.navigation.routes.Login
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.BirthdayCalendar
import it.giovanni.hub.ui.items.HubAlertDialog
import it.giovanni.hub.ui.items.backupEnabledIcon
import it.giovanni.hub.ui.items.backupInactiveIcon
import it.giovanni.hub.ui.items.backupDisabledIcon

@Composable
fun BirthdayScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    viewModel: BirthdayViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val isLoggedIn by mainViewModel.isGoogleLoggedIn.collectAsStateWithLifecycle()
    val isBackupEnabled by viewModel.isBackupEnabled.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()

    var searchResult by remember { mutableStateOf("") }
    val textFieldsViewModel: TextFieldsViewModel = viewModel()

    val showPermissionDeniedDialog = remember { mutableStateOf(false) }
    val showLoginDialog = remember { mutableStateOf(false) }
    val showEnableBackupDialog = remember { mutableStateOf(false) }
    val showDisableBackupDialog = remember { mutableStateOf(false) }

    fun hasCalendarPermissions(): Boolean {
        val readGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED

        val writeGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED

        return readGranted && writeGranted
    }

    val calendarPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val readGranted = permissions[Manifest.permission.READ_CALENDAR] == true
        val writeGranted = permissions[Manifest.permission.WRITE_CALENDAR] == true

        if (readGranted && writeGranted) {
            viewModel.enableBackup()
        } else {
            showPermissionDeniedDialog.value = true
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, isLoggedIn, isBackupEnabled) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && isLoggedIn && isBackupEnabled) {
                viewModel.importIfBackupEnabled()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.memo),
        topics = listOf("Room Database"),
        placeholder = "Search birthday by name...",
        showSearch = true,
        showBackup = true,
        isLoggedIn = isLoggedIn,
        isBackupEnabled = isBackupEnabled,
        onSearchResult = { result -> searchResult = result },
        onCloseResult = { searchResult = "" },
        onBackupResult = {
            when {
                !isLoggedIn -> {
                    showLoginDialog.value = true
                }

                !isBackupEnabled -> {
                    showEnableBackupDialog.value = true
                }

                else -> {
                    showDisableBackupDialog.value = true
                }
            }
        }
    ) { paddingValues ->

        val allBirthdays: List<MemoEntity> by viewModel.birthdays.collectAsState()
        val searchedBirthdays: List<MemoEntity> by viewModel.searchResults.collectAsState()

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

        var fabExpanded by remember { mutableStateOf(false) }
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

        val showViewDialog = remember { mutableStateOf(false) }
        val showAddDialog = remember { mutableStateOf(false) }
        val showEditDialog = remember { mutableStateOf(false) }
        var editingBirthday by remember { mutableStateOf<MemoEntity?>(null) }

        val showDeleteDialog = remember { mutableStateOf(false) }
        var deletingBirthday by remember { mutableStateOf<MemoEntity?>(null) }

        var deleteDialogDayKey by remember { mutableStateOf<Int?>(null) }

        val memo = remember { mutableStateOf(TextFieldValue("")) }

        fun resetFields() {
            memo.value = TextFieldValue("")
        }

        val birthdaysByMonthDay: Map<Int, List<MemoEntity>> = remember(allBirthdays) {
            allBirthdays.groupBy { it.month * 100 + it.day }
        }

        val selectedBirthdays: List<MemoEntity> = remember(selectedDate, birthdaysByMonthDay) {
            val date = selectedDate ?: return@remember emptyList()
            birthdaysByMonthDay[date.monthValue * 100 + date.dayOfMonth].orEmpty()
        }

        val hasBirthdaysInSelection = selectedBirthdays.isNotEmpty()
        val hasSelection = selectedDate != null

        LaunchedEffect(
            showDeleteDialog.value,
            deleteDialogDayKey,
            birthdaysByMonthDay
        ) {
            if (!showDeleteDialog.value) {
                val key = deleteDialogDayKey
                val isDayEmpty = key != null && birthdaysByMonthDay[key].orEmpty().isEmpty()

                if (isDayEmpty) {
                    deleteDialogDayKey = null
                }
            }
        }

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

        ExpandableBirthdayFAB(
            paddingValues = paddingValues,
            expanded = fabExpanded && hasSelection,
            hasSelection = hasSelection,
            hasBirthdaysInSelection = hasBirthdaysInSelection,
            onExpandedChange = { fabExpanded = it },
            onView = {
                showViewDialog.value = true
            },
            onAdd = {
                resetFields()
                showAddDialog.value = true
            }
        )

        ViewBirthdayDialog(
            showDialog = showSearchDialog,
            title = if (lastSearchText.isBlank()) "Search results"
            else "Birthdays matching \"$lastSearchText\"",
            birthdays = searchedBirthdays,
            onEdit = { picked ->
                editingBirthday = picked
                memo.value = TextFieldValue(picked.memo)
                showEditDialog.value = true
            },
            onDelete = { picked ->
                deletingBirthday = picked
                deleteDialogDayKey = picked.month * 100 + picked.day
                showDeleteDialog.value = true
            },
            onDismissRequest = {
                showSearchDialog.value = false
                searchResult = ""
                textFieldsViewModel.updateSearchTextState("")
                textFieldsViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
            }
        )

        ViewBirthdayDialog(
            showDialog = showViewDialog,
            title = "Birthdays in this day",
            birthdays = selectedBirthdays,
            onEdit = { picked ->
                editingBirthday = picked
                memo.value = TextFieldValue(picked.memo)
                showEditDialog.value = true
            },
            onDelete = { picked ->
                deletingBirthday = picked
                deleteDialogDayKey = picked.month * 100 + picked.day
                showDeleteDialog.value = true
            },
            onDismissRequest = { showViewDialog.value = false }
        )

        AddEditBirthdayDialog(
            title = "Add Birthday",
            icon = addIcon(),
            confirmButtonText = "Create",
            showDialog = showAddDialog,
            memo = memo,
            onDismissRequest = {
                showAddDialog.value = false
                resetFields()
            },
            onConfirmation = {
                showAddDialog.value = false

                val date = selectedDate ?: return@AddEditBirthdayDialog

                viewModel.createBirthday(
                    MemoEntity(
                        memo = memo.value.text,
                        month = date.monthValue,
                        day = date.dayOfMonth,
                        time = "12:00", // todo: add current time
                    )
                )
                resetFields()
            }
        )

        AddEditBirthdayDialog(
            title = "Edit Memo",
            icon = editIcon(),
            confirmButtonText = "Update",
            showDialog = showEditDialog,
            memo = memo,
            onDismissRequest = {
                showEditDialog.value = false
                resetFields()
            },
            onConfirmation = {
                showEditDialog.value = false

                val old = editingBirthday ?: return@AddEditBirthdayDialog
                val date = selectedDate ?: return@AddEditBirthdayDialog

                viewModel.updateBirthday(
                    old.copy(
                        memo = memo.value.text,
                        month = date.monthValue,
                        day = date.dayOfMonth,
                        time = "12:00", // todo: add current time
                    )
                )
                resetFields()
            }
        )

        DeleteBirthdayDialog(
            showDeleteDialog = showDeleteDialog,
            pendingDeleteBirthday = deletingBirthday,
            onPendingDeleteBirthdayChange = { deletingBirthday = it },
            onConfirmDelete = { b -> viewModel.deleteBirthday(b) }
        )

        HubAlertDialog(
            icon = backupInactiveIcon(),
            title = "Permission required",
            text = "Calendar permissions are required to enable the backup",
            showDialog = showPermissionDeniedDialog,
            onDismissRequest = {
                showPermissionDeniedDialog.value = false
            },
            onConfirmation = {
                showPermissionDeniedDialog.value = false
            }
        )

        HubAlertDialog(
            icon = backupInactiveIcon(),
            title = "Login required",
            text = "Google login is required to enable the backup",
            showDialog = showLoginDialog,
            onDismissRequest = {
                showLoginDialog.value = false
            },
            onConfirmation = {
                showLoginDialog.value = false

                mainViewModel.saveLoginState(state = false)

                navController.popBackStack()
                navController.navigate(route = Login) {
                    popUpTo(route = Login)
                }
            }
        )

        HubAlertDialog(
            icon = backupDisabledIcon(),
            title = "Enable backup",
            text = "Enable Google Calendar backup for all birthdays?",
            showDialog = showEnableBackupDialog,
            onDismissRequest = {
                showEnableBackupDialog.value = false
            },
            onConfirmation = {
                showEnableBackupDialog.value = false

                if (hasCalendarPermissions()) {
                    viewModel.enableBackup()
                } else {
                    calendarPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR
                        )
                    )
                }
            }
        )

        HubAlertDialog(
            icon = backupEnabledIcon(),
            title = "Disable backup",
            text = "Confirm you want to disable the backup and remove app-managed birthday events from calendar?",
            showDialog = showDisableBackupDialog,
            onDismissRequest = {
                showDisableBackupDialog.value = false
            },
            onConfirmation = {
                showDisableBackupDialog.value = false
                viewModel.disableBackup()
            }
        )
    }
}