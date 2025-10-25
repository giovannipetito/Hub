package it.giovanni.hub.presentation.screen.detail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

@Composable
fun Detail4Screen(
    navController: NavController,
    personViewModel: PersonViewModel
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.detail_4),
    topics = listOf("ViewModel", "LaunchedEffect", "rememberScrollState", "forEach")
) {
    val person: State<Person?> = personViewModel.person
    val firstName: String? = person.value?.firstName
    val lastName: String? = person.value?.lastName

    LaunchedEffect(key1 = person) {
        Log.i("[Person]", person.value?.firstName + " " + person.value?.lastName)
    }

    val list = personViewModel.list

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.clickable {
                navController.popBackStack()
            },
            text = "$firstName $lastName",
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.displayMedium.fontSize,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Button(
            onClick = {
                personViewModel.addRandomPerson()
            }
        ) {
            Text(
                text = "Add Random Person",
            )
        }
        list.forEach { person ->
            Text(
                text = person.firstName + " " + person.lastName,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}