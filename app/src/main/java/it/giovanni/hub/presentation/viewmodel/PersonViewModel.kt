package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import it.giovanni.hub.data.model.Person

class PersonViewModel : ViewModel() {

    private var _person: MutableState<Person?> = mutableStateOf(null)
    val person: State<Person?> = _person

    fun addPerson(newPerson: Person?) {
        _person.value = newPerson
    }
}