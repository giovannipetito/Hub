package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.entity.BirthdayEntity
import it.giovanni.hub.data.repository.local.BirthdayRepository
import it.giovanni.hub.domain.usecase.DeleteImportedGoogleEventUseCase
import it.giovanni.hub.domain.usecase.DisableBirthdayBackupUseCase
import it.giovanni.hub.domain.usecase.EnableBirthdayBackupUseCase
import it.giovanni.hub.domain.usecase.ImportGoogleCalendarEventsUseCase
import it.giovanni.hub.domain.usecase.ObserveBirthdayBackupEnabledUseCase
import it.giovanni.hub.domain.usecase.UpdateImportedGoogleEventUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    observeBackupEnabledUseCase: ObserveBirthdayBackupEnabledUseCase,
    private val enableBirthdayBackupUseCase: EnableBirthdayBackupUseCase,
    private val disableBirthdayBackupUseCase: DisableBirthdayBackupUseCase,
    private val importGoogleCalendarEventsUseCase: ImportGoogleCalendarEventsUseCase,
    private val deleteImportedGoogleEventUseCase: DeleteImportedGoogleEventUseCase,
    private val updateImportedGoogleEventUseCase: UpdateImportedGoogleEventUseCase,
    private val repository: BirthdayRepository
) : ViewModel() {

    companion object {
        const val GOOGLE_CALENDAR_SOURCE = "GOOGLE_CALENDAR"
    }

    private val _birthdays: MutableStateFlow<List<BirthdayEntity>> = MutableStateFlow(emptyList())
    val birthdays: StateFlow<List<BirthdayEntity>> = _birthdays.asStateFlow()

    private val _searchResults = MutableStateFlow<List<BirthdayEntity>>(emptyList())
    val searchResults: StateFlow<List<BirthdayEntity>> = _searchResults.asStateFlow()

    private var lastSearchQuery: String = ""

    val isBackupEnabled: StateFlow<Boolean> =
        observeBackupEnabledUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    init {
        refreshAllBirthdays()
    }

    fun refreshAllBirthdays() {
        viewModelScope.launch(Dispatchers.IO) {
            _birthdays.value = repository.readBirthdays(search = "")
        }
    }

    fun searchBirthdays(query: String) {
        lastSearchQuery = query.trim()
        viewModelScope.launch(Dispatchers.IO) {
            _searchResults.value =
                if (lastSearchQuery.isBlank()) {
                    emptyList()
                } else {
                    repository.readBirthdays(search = lastSearchQuery)
                }
        }
    }

    fun clearSearch() {
        lastSearchQuery = ""
        viewModelScope.launch {
            _searchResults.value = emptyList()
        }
    }

    fun exportIfBackupEnabled() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isBackupEnabled.value) return@launch
            if (_isSyncing.value) return@launch

            _isSyncing.value = true
            try {
                val allBirthdays = repository.readBirthdays(search = "")
                enableBirthdayBackupUseCase(allBirthdays)
                refreshUiState()
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun importIfBackupEnabled() {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isBackupEnabled.value) return@launch
            if (_isSyncing.value) return@launch

            _isSyncing.value = true
            try {
                importGoogleCalendarEventsUseCase()
                refreshUiState()
            } finally {
                _isSyncing.value = false
            }
        }
    }

    private suspend fun refreshUiState() {
        _birthdays.value = repository.readBirthdays(search = "")

        _searchResults.value =
            if (lastSearchQuery.isBlank()) {
                emptyList()
            } else {
                repository.readBirthdays(search = lastSearchQuery)
            }
    }

    private fun refreshAfterMutationAndSyncIfNeeded() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshUiState()

            if (isBackupEnabled.value) {
                _isSyncing.value = true
                try {
                    val allBirthdays = repository.readBirthdays(search = "")
                    enableBirthdayBackupUseCase(allBirthdays)
                    importGoogleCalendarEventsUseCase()
                    refreshUiState()
                } finally {
                    _isSyncing.value = false
                }
            }
        }
    }

    fun createBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createBirthday(birthdayEntity)
            refreshAfterMutationAndSyncIfNeeded()
        }
    }

    fun updateBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (birthdayEntity.externalSource == GOOGLE_CALENDAR_SOURCE &&
                birthdayEntity.externalEventId != null
            ) {
                val updatedInGoogle = updateImportedGoogleEventUseCase(birthdayEntity)

                if (updatedInGoogle) {
                    repository.updateBirthday(birthdayEntity)
                    importIfBackupEnabled()
                } else {
                    refreshUiState()
                }
            } else {
                repository.updateBirthday(birthdayEntity)
                refreshAfterMutationAndSyncIfNeeded()
            }
        }
    }

    fun deleteBirthdaysForDay(month: Int, day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val dayItems = repository.readBirthdaysForDay(month, day)

            dayItems.forEach { item ->
                if (item.externalSource == GOOGLE_CALENDAR_SOURCE && item.externalEventId != null) {
                    deleteImportedGoogleEventUseCase(item.externalEventId)
                }
                repository.deleteBirthday(item)
            }

            if (isBackupEnabled.value) {
                importIfBackupEnabled()
                exportIfBackupEnabled()
            } else {
                refreshUiState()
            }
        }
    }

    fun deleteBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (birthdayEntity.externalSource == GOOGLE_CALENDAR_SOURCE &&
                birthdayEntity.externalEventId != null
            ) {
                val deletedFromGoogle = deleteImportedGoogleEventUseCase(
                    birthdayEntity.externalEventId
                )

                if (deletedFromGoogle) {
                    repository.deleteBirthday(birthdayEntity)
                    importIfBackupEnabled()
                } else {
                    refreshUiState()
                }
            } else {
                repository.deleteBirthday(birthdayEntity)
                refreshAfterMutationAndSyncIfNeeded()
            }
        }
    }

    fun enableBackup() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isSyncing.value) return@launch

            _isSyncing.value = true
            try {
                val birthdays = repository.readBirthdays(search = "")
                enableBirthdayBackupUseCase(birthdays)
                importGoogleCalendarEventsUseCase()
                refreshUiState()
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun disableBackup() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isSyncing.value) return@launch
            _isSyncing.value = true
            try {
                disableBirthdayBackupUseCase()
                refreshUiState()
            } finally {
                _isSyncing.value = false
            }
        }
    }
}