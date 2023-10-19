package it.giovanni.hub.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.Graph.AUTH_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import it.giovanni.hub.navigation.screen.Screen

@Composable
fun HomeScreen(navController: NavController, mainActivity: MainActivity) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.deep_purple_200)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier.clickable {
                    // navController.navigate(route = Screen.Detail1.route) // Per navigare senza passare parametri.
                    navController.navigate(route = Screen.Detail1.passRequiredArguments(6, "Giovanni"))
                },
                text = "Open Detail 1",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp, // MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    // navController.navigate(route = Screen.Detail2.route) // Per navigare senza passare parametri.
                    navController.navigate(route = Screen.Detail2.passOptionalArguments(
                        name = "Giovanni"
                    ))
                },
                text = "Open Detail 2",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(AUTH_ROUTE)
                },
                text = "Auth/Sign Up",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.TextFields.route)
                },
                text = "Text Fields",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.Users.route)
                },
                text = "Users",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.UsersRx.route)
                },
                text = "Users Rx",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.UI.route)
                },
                text = "UI",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController(), mainActivity = MainActivity()
    )
}