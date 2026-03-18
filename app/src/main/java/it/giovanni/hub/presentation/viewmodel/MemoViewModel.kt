package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.entity.MemoEntity
import it.giovanni.hub.data.repository.local.MemoRepository
import it.giovanni.hub.domain.usecase.DeleteImportedGoogleEventUseCase
import it.giovanni.hub.domain.usecase.DisableBackupUseCase
import it.giovanni.hub.domain.usecase.EnableBackupUseCase
import it.giovanni.hub.domain.usecase.ImportGoogleCalendarEventsUseCase
import it.giovanni.hub.domain.usecase.ObserveBackupEnabledUseCase
import it.giovanni.hub.domain.usecase.SetBackupEnabledUseCase
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
class MemoViewModel @Inject constructor(
    observeBackupEnabledUseCase: ObserveBackupEnabledUseCase,
    private val enableBackupUseCase: EnableBackupUseCase,
    private val disableBackupUseCase: DisableBackupUseCase,
    private val importGoogleCalendarEventsUseCase: ImportGoogleCalendarEventsUseCase,
    private val deleteImportedGoogleEventUseCase: DeleteImportedGoogleEventUseCase,
    private val updateImportedGoogleEventUseCase: UpdateImportedGoogleEventUseCase,
    private val setBackupEnabledUseCase: SetBackupEnabledUseCase,
    private val repository: MemoRepository
) : ViewModel() {

    companion object {
        const val GOOGLE_CALENDAR_SOURCE = "GOOGLE_CALENDAR"
    }

    private val _memos: MutableStateFlow<List<MemoEntity>> = MutableStateFlow(emptyList())
    val memos: StateFlow<List<MemoEntity>> = _memos.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MemoEntity>>(emptyList())
    val searchResults: StateFlow<List<MemoEntity>> = _searchResults.asStateFlow()

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
        refreshAllMemos()
    }

    fun refreshAllMemos() {
        viewModelScope.launch(Dispatchers.IO) {
            _memos.value = repository.readMemos(search = "")
        }
    }

    fun searchMemos(query: String) {
        lastSearchQuery = query.trim()
        viewModelScope.launch(Dispatchers.IO) {
            _searchResults.value =
                if (lastSearchQuery.isBlank()) {
                    emptyList()
                } else {
                    repository.readMemos(search = lastSearchQuery)
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
                val allMemos = repository.readMemos(search = "")
                enableBackupUseCase(allMemos)
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
        _memos.value = repository.readMemos(search = "")

        _searchResults.value =
            if (lastSearchQuery.isBlank()) {
                emptyList()
            } else {
                repository.readMemos(search = lastSearchQuery)
            }
    }

    private fun refreshAfterMutationAndSyncIfNeeded() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshUiState()

            if (isBackupEnabled.value) {
                _isSyncing.value = true
                try {
                    val allMemos = repository.readMemos(search = "")
                    enableBackupUseCase(allMemos)
                    importGoogleCalendarEventsUseCase()
                    refreshUiState()
                } finally {
                    _isSyncing.value = false
                }
            }
        }
    }

    fun createMemo(memoEntity: MemoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createMemo(memoEntity)
            refreshAfterMutationAndSyncIfNeeded()
        }
    }

    fun updateMemo(memoEntity: MemoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (memoEntity.externalSource == GOOGLE_CALENDAR_SOURCE &&
                memoEntity.externalEventId != null
            ) {
                val updatedInGoogle = updateImportedGoogleEventUseCase(memoEntity)

                if (updatedInGoogle) {
                    repository.updateMemo(memoEntity)
                    importIfBackupEnabled()
                } else {
                    refreshUiState()
                }
            } else {
                repository.updateMemo(memoEntity)
                refreshAfterMutationAndSyncIfNeeded()
            }
        }
    }

    fun deleteMemo(memoEntity: MemoEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (memoEntity.externalSource == GOOGLE_CALENDAR_SOURCE &&
                memoEntity.externalEventId != null
            ) {
                val deletedFromGoogle = deleteImportedGoogleEventUseCase(
                    memoEntity.externalEventId
                )

                if (deletedFromGoogle) {
                    repository.deleteMemo(memoEntity)
                    importIfBackupEnabled()
                } else {
                    refreshUiState()
                }
            } else {
                repository.deleteMemo(memoEntity)
                refreshAfterMutationAndSyncIfNeeded()
            }
        }
    }

    fun enableBackup() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isSyncing.value) return@launch

            _isSyncing.value = true
            try {
                val localMemos = repository.readMemos(search = "")
                val shouldRestoreAppManaged = localMemos.isEmpty()

                if (shouldRestoreAppManaged) {
                    setBackupEnabledUseCase(true)
                    importGoogleCalendarEventsUseCase(restoreAppManagedEvents = true)
                } else {
                    enableBackupUseCase(localMemos)

                    importGoogleCalendarEventsUseCase(
                        restoreAppManagedEvents = false
                    )
                }

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
                disableBackupUseCase()
                refreshUiState()
            } finally {
                _isSyncing.value = false
            }
        }
    }
}