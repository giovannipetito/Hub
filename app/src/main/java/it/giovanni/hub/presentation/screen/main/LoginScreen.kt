package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.Graph
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import it.giovanni.hub.navigation.util.set.WelcomeSet
import it.giovanni.hub.ui.items.GoogleButton

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
        ) {
            Text(
                text = "Login",
                color = Color.Blue,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    modifier = Modifier
                        .size(144.dp)
                        .clip(RoundedCornerShape(size = 12.dp))
                        .border(
                            width = 4.dp,
                            color = Color.Cyan,
                            shape = RoundedCornerShape(size = 12.dp)
                        ),
                    painter = painterResource(
                        id = R.drawable.giovanni),
                    contentDescription = "Rounded Corner Image"
                )
                GoogleButton(
                    text = "Sign Up with Google",
                    loadingText = "Creating Account",
                    onClicked = {
                        navController.popBackStack()
                        navController.navigate(Graph.MAIN_ROUTE) {
                            popUpTo(Graph.MAIN_ROUTE)
                        }
                    }
                )
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate(route = WelcomeSet.Info.route)
                    },
                    text = "Info",
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
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController(), mainActivity = MainActivity())
}