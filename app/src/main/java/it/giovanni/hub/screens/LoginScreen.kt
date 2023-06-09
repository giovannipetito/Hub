package it.giovanni.hub.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import it.giovanni.hub.Constants.HOME_ROUTE
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import it.giovanni.hub.navigation.Screen

@Composable
fun LoginScreen(navController: NavController, mainActivity: MainActivity) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue_200)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.SignUp.route)
                },
                text = "Login",
                color = Color.Blue,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {

                    // 1)
                    // navController.popBackStack()

                    // 2)
                    navController.navigate(HOME_ROUTE) {
                        popUpTo(HOME_ROUTE)
                    }

                    // Nota: è preferibile la soluzione 2 perché vogliamo tornare indietro
                    // in HomeScreen che è la prima schermata di un'altra route: HOME_ROUTE.
                },
                text = "Go Back",
                color = Color.Blue,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    // Aggiungendo navController.popBackStack() facciamo il pop di LoginScreen dal
                    // back stack. In Questo modo, tornando indietro da Detail2Screen, andiamo in
                    // HomeScreen invece che LoginScreen.
                    navController.popBackStack()
                    navController.navigate(Screen.Detail2.passOptionalArguments())
                },
                text = "Open Detail 2",
                color = Color.Blue,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController(), mainActivity = MainActivity())
}