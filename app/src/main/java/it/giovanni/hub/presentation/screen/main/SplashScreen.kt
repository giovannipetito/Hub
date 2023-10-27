package it.giovanni.hub.presentation.screen.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.Graph
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, mainActivity: MainActivity) {
    var startAnimation by remember {
        mutableStateOf(false)
    }
    val alphaAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 3000),
        label = ""
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        navController.popBackStack()
        navController.navigate(Graph.WIZARD_ROUTE) {
            popUpTo(Graph.WIZARD_ROUTE)
        }
    }
    Splash(alphaAnimation = alphaAnimation.value)
}

@Composable
fun Splash(alphaAnimation: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = if (isSystemInDarkTheme()) Color.Black else Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(144.dp)
                .clip(CircleShape)
                .border(
                    width = 4.dp,
                    color = Color.Cyan,
                    shape = CircleShape
                )
                .alpha(alphaAnimation),
            painter = painterResource(
                id = R.drawable.giovanni),
            contentDescription = "Circular Image"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController(), mainActivity = MainActivity())
}