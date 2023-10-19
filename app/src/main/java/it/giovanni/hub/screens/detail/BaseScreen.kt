package it.giovanni.hub.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.Graph.HOME_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R

// State: si definisce State qualsiasi valore che può cambiare nel tempo.
// Event: notifica a una parte di un programma che è successo qualcosa.

@Composable
fun BaseScreen(navController: NavController, mainActivity: MainActivity) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue_200)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Base",
                color = Color.Blue,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate(HOME_ROUTE) {
                            popUpTo(HOME_ROUTE)
                        }
                    }
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = Modifier.clickable {
                    },
                    text = "Base",
                    color = Color.Blue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseScreenPreview() {
    BaseScreen(navController = rememberNavController(), mainActivity = MainActivity())
}