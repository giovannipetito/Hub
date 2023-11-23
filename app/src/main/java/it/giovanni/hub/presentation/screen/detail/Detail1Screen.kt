package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Detail1Screen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.clickable {

                // Non rimuove Detail dal back stack.
                // navController.navigate(route = MainSet.Profile.route)

                // Non è in grado di passare argomenti.
                navController.popBackStack()

                // è in grado di passare argomenti.
                /*
                navController.navigate(route = MainSet.Profile.route) {
                    popUpTo(MainSet.Profile.route) {
                        inclusive = true
                    }
                }
                */
            },
            text = "Detail 1",
            color = Color.Red,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Detail1ScreenPreview() {
    Detail1Screen(navController = rememberNavController())
}