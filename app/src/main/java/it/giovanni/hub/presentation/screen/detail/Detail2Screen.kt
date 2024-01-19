package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R

@Composable
fun Detail2Screen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.detail_2),
    topics = listOf("Modifier.clickable", "popBackStack")
) {
    // This Box overrides the one that wraps the content in BaseScreen.
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 24.dp),
        contentAlignment = Alignment.TopCenter
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
                    popUpTo(ProfileRoutes.Profile.route) {
                        inclusive = true
                    }
                }
                */
            },
            text = "Detail 2",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Detail2ScreenPreview() {
    Detail2Screen(navController = rememberNavController())
}