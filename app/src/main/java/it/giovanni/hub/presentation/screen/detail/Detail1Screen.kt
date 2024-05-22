package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R

@Composable
fun Detail1Screen(
    navController: NavController,
    id: Int,
    name: String
) {
    val topics: List<String> = listOf("Modifier.clickable", "popBackStack")

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.detail_1),
        topics = topics
    ) {
        // This Column represents the content wrapped by the Box in BaseScreen.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.clickable {

                    // Non rimuove Detail dal back stack.
                    // navController.navigate(route = ProfileRoutes.Profile.route)

                    // Non è in grado di passare argomenti.
                    navController.popBackStack()

                    // è in grado di passare argomenti.
                    /*
                    navController.navigate(route = ProfileRoutes.Profile.route) {
                        popUpTo(route = ProfileRoutes.Profile.route) {
                            inclusive = true
                        }
                    }
                    */
                },
                text = "Detail 1",
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Id: $id, name: $name",
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Detail1ScreenPreview() {
    Detail1Screen(navController = rememberNavController(), id = 1, name = "Giovanni")
}