package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.entity.BirthdayEntity
import it.giovanni.hub.data.repository.local.BirthdayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    private val repository: BirthdayRepository
) : ViewModel() {

    private val _birthdays: MutableStateFlow<List<BirthdayEntity>> = MutableStateFlow(emptyList())
    val birthdays: StateFlow<List<BirthdayEntity>> = _birthdays.asStateFlow()

    // private var currentSearch: String = ""
    private val _searchResults = MutableStateFlow<List<BirthdayEntity>>(emptyList())
    val searchResults: StateFlow<List<BirthdayEntity>> = _searchResults.asStateFlow()

    private var lastSearchQuery: String = ""

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
                if (lastSearchQuery.isBlank())
                    emptyList()
                else
                    repository.readBirthdays(search = lastSearchQuery)
        }
    }

    fun clearSearch() {
        lastSearchQuery = ""
        viewModelScope.launch {
            _searchResults.value = emptyList()
        }
    }

    private fun refreshAfterMutation() {
        viewModelScope.launch(Dispatchers.IO) {
            // refresh calendar
            _birthdays.value = repository.readBirthdays(search = "")

            // refresh search dialog results too (if a search is active)
            if (lastSearchQuery.isNotBlank()) {
                _searchResults.value = repository.readBirthdays(search = lastSearchQuery)
            }
        }
    }

    fun createBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createBirthday(birthdayEntity)
            refreshAfterMutation()
        }
    }

    fun updateBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBirthday(birthdayEntity)
            refreshAfterMutation()
        }
    }

    fun deleteBirthdaysForDay(month: Int, day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBirthdaysForDay(month, day)
            refreshAfterMutation()
        }
    }

    fun deleteBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBirthday(birthdayEntity)
            refreshAfterMutation()
        }
    }
}