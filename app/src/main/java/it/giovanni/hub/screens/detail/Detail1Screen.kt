package it.giovanni.hub.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R

@Composable
fun Detail1Screen(navController: NavController, mainActivity: MainActivity) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.red_200)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.clickable {

                // non rimuove Detail dal back stack
                // navController.navigate(route = MainSet.Home.route)

                // non è in grado di passare argomenti.
                navController.popBackStack()

                // è in grado di passare argomenti.
                /*
                navController.navigate(route = MainSet.Home.route) {
                    popUpTo(MainSet.Home.route) {
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
    Detail1Screen(navController = rememberNavController(), mainActivity = MainActivity())
}