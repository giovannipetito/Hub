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
        Person("Giovanni", "Petito", visibility = true),
        Person("Tara", "Tandel", visibility = true),
        Person("Angelina", "Basile", visibility = true),
        Person("Vincenzo", "Petito", visibility = true),
        Person("Raffaele", "Petito", visibility = true),
        Person("Teresa", "Petito", visibility = true),
        Person("Salvatore", "Pragliola", visibility = true),
        Person("Ilenia", "Pragliola", visibility = true),
        Person("Armando", "Pragliola", visibility = true)
    )

    // mutableStateListOf è observable e il suo utilizzo ci permette di fare il compose dello
    // screen con successo
    private var _list = mutableStateListOf<Person>()
    val list: List<Person> = _list

    fun addRandomPerson() {
        val randomPerson: Person = randomList.random()
        _list.add(randomPerson)

        val randomId = Random.nextInt(from = 1, until = 100)
        Log.i("[Person]", "" + randomId + ") " + _list.last().firstName + " " + _list.last().lastName)
    }
}