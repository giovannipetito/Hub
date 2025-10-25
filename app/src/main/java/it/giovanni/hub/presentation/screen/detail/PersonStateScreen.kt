package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.presentation.viewmodel.PersonEvent
import it.giovanni.hub.presentation.viewmodel.PersonStateViewModel
import it.giovanni.hub.ui.items.cards.PersonCard
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PersonStateScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.state_and_events),
    topics = listOf("viewModel", "StateFlow", "State", "rememberSaveable")
) {
    val viewModel: PersonStateViewModel = viewModel()

    val stateFlow: StateFlow<Person> = viewModel.personState
    val state: State<Person> = stateFlow.collectAsState()

    val visibility = remember { mutableStateOf(state.value.visibility) }

    var person1 by rememberSaveable {
        mutableStateOf(Person(id = 1, firstName = "Giovanni", lastName = "Petito", visibility = true))
    }

    var person2 by rememberSaveable(stateSaver = PersonSaver) {
        mutableStateOf(Person(id = 2, firstName = "Giovanni", lastName = "Petito", visibility = true))
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        item {
            if (visibility.value)
                PersonCard(person = person1, modifier = Modifier)

            Text(text = person1.firstName + " " + person1.lastName)

            Spacer(modifier = Modifier.height(height = 12.dp))

            Text(text = person2.firstName + " " + person2.lastName)

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
                onClick = {
                    // 1° solution
                    // state.value.visibility = state.value.visibility.not()
                    // visibility.value = state.value.visibility

                    // 2° solution
                    if (visibility.value) {
                        viewModel.personEvent(PersonEvent.HidePerson)
                    } else {
                        viewModel.personEvent(PersonEvent.ShowPerson)
                    }
                    visibility.value = state.value.visibility
                }
            ) {
                Text(text = "Change Visibility")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
                onClick = {
                    person1 = Person(id = 1, "Leonardo", "Petito", true)
                    person2 = Person(id = 2, "Leonardo", "Petito", true)
                }
            ) {
                Text(text = "Update person")
            }
        }
    }
}

object PersonSaver: Saver<Person, Map<String, Any>> {

    override fun SaverScope.save(value: Person): Map<String, Any> {
        return mapOf(
            "firstName" to value.firstName,
            "lastName" to value.lastName,
            "visibility" to value.visibility
        )
    }

    override fun restore(value: Map<String, Any>): Person {

        val id = value["id"] as Int
        val firstName = value["firstName"] as String
        val lastName = value["lastName"] as String
        val visibility = value["visibility"] as Boolean

        return Person(id = id, firstName = firstName, lastName = lastName, visibility = visibility)
    }
}

@Preview(showBackground = true)
@Composable
fun PersonStateScreenPreview() {
    PersonStateScreen(navController = rememberNavController())
}