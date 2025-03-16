package it.giovanni.hub.presentation.screen.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.navigation.Wizard
import it.giovanni.hub.navigation.util.routes.MainRoutes
import it.giovanni.hub.presentation.viewmodel.LoadingViewModel
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.circles.LoadingIcons
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    viewModel: LoadingViewModel = hiltViewModel(),
    onSplashLoaded: () -> Unit
) {
    viewModel.KeepOrientationPortrait()
    val context = LocalContext.current

    var splashLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = Unit) {
        delay(2000)
        splashLoading = false
        onSplashLoaded()
    }

    if (!splashLoading) {
        LaunchedEffect(key1 = true) {
            delay(2000)

            navController.popBackStack()

            if (mainViewModel.getSignedInUser() != null) {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                navController.navigate(route = MainRoutes.Home.route) {
                    popUpTo(route = MainRoutes.Home.route)
                }
            } else {
                val screen: String by viewModel.startDestination
                if (screen == "Wizard") {
                    navController.navigate(route = Wizard) {
                        popUpTo(route = Wizard)
                    }
                } else {
                    navController.navigate(route = screen) {
                        popUpTo(route = screen)
                    }
                }
            }
        }
        LoadingScreenContent()
    }
}

@Composable
fun LoadingScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LoadingIcons(
            circleIcons = listOf(
                R.drawable.ico_dragon_ball_1,
                R.drawable.ico_dragon_ball_4,
                R.drawable.ico_dragon_ball_7
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen(
        navController = rememberNavController(),
        mainViewModel = hiltViewModel(),
        viewModel = hiltViewModel(),
        onSplashLoaded = {}
    )
}