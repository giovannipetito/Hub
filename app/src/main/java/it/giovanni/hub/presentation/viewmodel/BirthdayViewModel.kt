package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.entity.BirthdayEntity
import it.giovanni.hub.data.repository.local.BirthdayRepository
import it.giovanni.hub.domain.usecase.DisableBirthdayBackupUseCase
import it.giovanni.hub.domain.usecase.EnableBirthdayBackupUseCase
import it.giovanni.hub.domain.usecase.ImportGoogleCalendarEventsUseCase
import it.giovanni.hub.domain.usecase.ObserveBirthdayBackupEnabledUseCase
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
    private val repository: BirthdayRepository
) : ViewModel() {

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

    private suspend fun refreshAfterMutationAndSyncIfNeeded() {
        val allBirthdays = repository.readBirthdays(search = "")
        _birthdays.value = allBirthdays

        if (lastSearchQuery.isNotBlank()) {
            _searchResults.value = repository.readBirthdays(search = lastSearchQuery)
        } else {
            _searchResults.value = emptyList()
        }

        if (isBackupEnabled.value) {
            _isSyncing.value = true
            try {
                enableBirthdayBackupUseCase(allBirthdays)
                importGoogleCalendarEventsUseCase()

                val refreshedBirthdays = repository.readBirthdays(search = "")
                _birthdays.value = refreshedBirthdays

                if (lastSearchQuery.isNotBlank()) {
                    _searchResults.value = repository.readBirthdays(search = lastSearchQuery)
                } else {
                    _searchResults.value = emptyList()
                }
            } finally {
                _isSyncing.value = false
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
            repository.updateBirthday(birthdayEntity)
            refreshAfterMutationAndSyncIfNeeded()
        }
    }

    fun deleteBirthdaysForDay(month: Int, day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBirthdaysForDay(month, day)
            refreshAfterMutationAndSyncIfNeeded()
        }
    }

    fun deleteBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBirthday(birthdayEntity)
            refreshAfterMutationAndSyncIfNeeded()
        }
    }

    fun enableBackup() {
        viewModelScope.launch(Dispatchers.IO) {
            _isSyncing.value = true
            try {
                val birthdays = repository.readBirthdays(search = "")
                enableBirthdayBackupUseCase(birthdays)
                importGoogleCalendarEventsUseCase()

                _birthdays.value = repository.readBirthdays(search = "")
                if (lastSearchQuery.isNotBlank()) {
                    _searchResults.value = repository.readBirthdays(search = lastSearchQuery)
                } else {
                    _searchResults.value = emptyList()
                }
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun disableBackup() {
        viewModelScope.launch(Dispatchers.IO) {
            _isSyncing.value = true
            try {
                disableBirthdayBackupUseCase()
            } finally {
                _isSyncing.value = false
            }
        }
    }
}