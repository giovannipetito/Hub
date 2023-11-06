package it.giovanni.hub.presentation.screen.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.MainActivity
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.presentation.viewmodel.PersonViewModel

@Composable
fun Detail4Screen(
    navController: NavController,
    mainActivity: MainActivity,
    personViewModel: PersonViewModel
) {
    val person: State<Person?> = personViewModel.person
    val firstName: String? = person.value?.firstName
    val lastName: String? = person.value?.lastName

    LaunchedEffect(key1 = person) {
        Log.i("[Person]", person.value?.firstName + " " + person.value?.lastName)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.clickable {
                navController.popBackStack()
            },
            text = "$firstName $lastName",
            color = Color.Red,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Detail4ScreenPreview() {
    Detail4Screen(
        navController = rememberNavController(),
        mainActivity = MainActivity(),
        personViewModel = PersonViewModel()
    )
}