package it.giovanni.hub.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.utils.Constants.mockedList
import kotlin.random.Random

class PersonViewModel : ViewModel() {

    private var _person: MutableState<Person?> = mutableStateOf(null)
    val person: State<Person?> = _person

    // mutableStateListOf è observable e il suo utilizzo ci permette di fare il compose dello
    // screen con successo.
    private var _list = mutableStateListOf<Person>()
    val list: List<Person> = _list

    fun addPerson(newPerson: Person?) {
        _person.value = newPerson
    }

    fun addRandomPerson() {
        val randomPerson: Person = mockedList.random()
        _list.add(randomPerson)

        val randomId = Random.nextInt(from = 1, until = 100)
        Log.i("[Person]", "" + randomId + ") " + _list.last().firstName + " " + _list.last().lastName)
    }
}