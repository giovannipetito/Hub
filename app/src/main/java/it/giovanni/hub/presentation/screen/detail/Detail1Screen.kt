package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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
        // This Text represents the content wrapped by the Box in BaseScreen.
        Text(
            modifier = Modifier.clickable {

                // Non rimuove Detail dal back stack.
                // navController.navigate(route = ProfileRoutes.Profile.route)

                // Non è in grado di passare argomenti.
                navController.popBackStack()

                // è in grado di passare argomenti.
                /*
                navController.navigate(route = ProfileRoutes.Profile.route) {
                    popUpTo(ProfileRoutes.Profile.route) {
                        inclusive = true
                    }
                }
                */
            },
            text = "Detail 1 " + name,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Detail1ScreenPreview() {
    Detail1Screen(navController = rememberNavController(), id = 1, name = "Giovanni")
}