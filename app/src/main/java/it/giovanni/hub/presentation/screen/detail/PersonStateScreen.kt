package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.presentation.viewmodel.PersonEvent
import it.giovanni.hub.presentation.viewmodel.PersonStateViewModel
import it.giovanni.hub.ui.items.cards.PersonCard
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PersonStateScreen(navController: NavController) {

    val topics: List<String> = listOf("viewModel", "StateFlow", "State")

    val viewModel: PersonStateViewModel = viewModel()

    val stateFlow: StateFlow<Person> = viewModel.personState
    val state: State<Person> = stateFlow.collectAsState()

    val visibility = remember { mutableStateOf(state.value.visibility) }

    val person = Person(firstName = "Giovanni", lastName = "Petito", visibility = true)

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.state_and_events),
        topics = topics
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {

                if (visibility.value)
                    PersonCard(person = person, modifier = Modifier)

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
                    Text(
                        text = "Change Visibility",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonStateScreenPreview() {
    PersonStateScreen(navController = rememberNavController())
}