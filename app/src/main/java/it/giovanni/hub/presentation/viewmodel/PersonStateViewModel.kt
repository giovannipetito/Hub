package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import it.giovanni.hub.domain.model.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class PersonEvent {
    data object ShowPerson: PersonEvent()
    data object HidePerson: PersonEvent()
}

class PersonStateViewModel : ViewModel() {

    private val _personState = MutableStateFlow(
        Person(id = 1, firstName = "Giovanni", lastName = "Petito", visibility = false)
    )
    val personState: StateFlow<Person> = _personState.asStateFlow()

    init {
        _personState.value.visibility = false
    }

    fun personEvent(event: PersonEvent) {
        when (event) {
            is PersonEvent.HidePerson -> {
                _personState.value.visibility = false
            }
            is PersonEvent.ShowPerson -> {
                _personState.value.visibility = true
            }
        }
    }
}