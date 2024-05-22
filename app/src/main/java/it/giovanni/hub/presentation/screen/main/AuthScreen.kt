package it.giovanni.hub.presentation.screen.main

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.navigation.Graph.PROFILE_ROUTE
import it.giovanni.hub.navigation.util.routes.AuthRoutes
import it.giovanni.hub.navigation.util.routes.ProfileRoutes

@Composable
fun AuthScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = AuthRoutes.SignUp)
                },
                text = "Authentication",
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {

                    // 1)
                    // navController.popBackStack()

                    // 2)
                    navController.navigate(route = PROFILE_ROUTE) {
                        popUpTo(route = PROFILE_ROUTE)
                    }

                    // Nota: è preferibile la soluzione 2 perché vogliamo tornare indietro
                    // in ProfileScreen che è la prima schermata di un'altra route: PROFILE_ROUTE.
                },
                text = "Go Back",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.clickable {
                    // Aggiungendo navController.popBackStack() facciamo il pop di AuthScreen dal
                    // back stack. In Questo modo, tornando indietro da Detail2Screen, andiamo in
                    // ProfileScreen invece che AuthScreen.
                    navController.popBackStack()
                    navController.navigate(route = ProfileRoutes.Detail2)
                },
                text = stringResource(id = R.string.detail_2),
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    AuthScreen(navController = rememberNavController())
}