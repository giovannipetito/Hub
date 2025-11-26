package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.navigation.routes.ProfileRoutes
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

@Composable
fun Detail1Screen(
    navController: NavController,
    personViewModel: PersonViewModel
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.detail_1),
    topics = listOf("ViewModel", "Spacer", "mutableStateOf", "listOf", "mutableStateListOf", "Random")
) {
    val person1: Person? = navController.previousBackStackEntry?.savedStateHandle?.get<Person>(key = "person")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.clickable {
                navController.popBackStack()
            },
            text = person1?.firstName + " " + person1?.lastName,
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.displayMedium.fontSize,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(height = 24.dp))

        Text(
            modifier = Modifier.clickable {
                // navController.popBackStack()
                val person2 = Person(id = 1, firstName = "Giovanni", lastName = "Petito", visibility = true)
                personViewModel.addPerson(newPerson = person2)
                navController.navigate(route = ProfileRoutes.Detail2)
            },
            text = stringResource(id = R.string.detail_2),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}