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

    private var currentSearch: String = ""

    init {
        readBirthdays(search = "")
    }

    fun readBirthdays(search: String) {
        currentSearch = search
        viewModelScope.launch(Dispatchers.IO) {
            _birthdays.value = repository.readBirthdays(search = currentSearch)
        }
    }

    fun createBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createBirthday(birthdayEntity)
            _birthdays.value = repository.readBirthdays(currentSearch)
        }
    }

    fun updateBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBirthday(birthdayEntity)
            _birthdays.value = repository.readBirthdays(currentSearch)
        }
    }

    fun deleteBirthdaysForDay(month: Int, day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBirthdaysForDay(month, day)
            _birthdays.value = repository.readBirthdays(currentSearch)
        }
    }

    fun deleteBirthday(birthdayEntity: BirthdayEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBirthday(birthdayEntity)
            _birthdays.value = repository.readBirthdays(currentSearch)
        }
    }
}