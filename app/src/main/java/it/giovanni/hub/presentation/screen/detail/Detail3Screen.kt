package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

@Composable
fun Detail3Screen(
    navController: NavController,
    personViewModel: PersonViewModel
) {
    val topics: List<String> = listOf(
        "ViewModel", "Spacer", "mutableStateOf", "listOf", "mutableStateListOf", "Random"
    )

    val person1: Person? = navController.previousBackStackEntry?.savedStateHandle?.get<Person>(key = "person")

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.detail_3),
        topics = topics
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.clickable {
                    navController.popBackStack()
                },
                text = person1?.firstName + " " + person1?.lastName,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                modifier = Modifier.clickable {
                    // navController.popBackStack()
                    val person2 = Person(firstName = "Giovanni", lastName = "Petito", visibility = true)
                    personViewModel.addPerson(newPerson = person2)
                    navController.navigate(route = MainSet.Detail4.route)
                },
                text = stringResource(id = R.string.detail_4),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Detail3ScreenPreview() {
    Detail3Screen(
        navController = rememberNavController(),
        personViewModel = PersonViewModel()
    )
}