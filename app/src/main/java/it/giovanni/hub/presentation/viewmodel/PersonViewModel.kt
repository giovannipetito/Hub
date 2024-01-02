package it.giovanni.hub.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import it.giovanni.hub.data.model.Person
import kotlin.random.Random

class PersonViewModel : ViewModel() {

    private var _person: MutableState<Person?> = mutableStateOf(null)
    val person: State<Person?> = _person

    fun addPerson(newPerson: Person?) {
        _person.value = newPerson
    }

    private val randomList: List<Person> = listOf(
        Person(id = 1, "Giovanni", "Petito", visibility = true),
        Person(id = 2, "Tara", "Tandel", visibility = true),
        Person(id = 3, "Angelina", "Basile", visibility = true),
        Person(id = 4, "Vincenzo", "Petito", visibility = true),
        Person(id = 5, "Raffaele", "Petito", visibility = true),
        Person(id = 6, "Teresa", "Petito", visibility = true),
        Person(id = 7, "Salvatore", "Pragliola", visibility = true),
        Person(id = 8, "Ilenia", "Pragliola", visibility = true),
        Person(id = 9, "Armando", "Pragliola", visibility = true)
    )

    // mutableStateListOf è observable e il suo utilizzo ci permette di fare il compose dello
    // screen con successo.
    private var _list = mutableStateListOf<Person>()
    val list: List<Person> = _list

    fun addRandomPerson() {
        val randomPerson: Person = randomList.random()
        _list.add(randomPerson)

        val randomId = Random.nextInt(from = 1, until = 100)
        Log.i("[Person]", "" + randomId + ") " + _list.last().firstName + " " + _list.last().lastName)
    }
}