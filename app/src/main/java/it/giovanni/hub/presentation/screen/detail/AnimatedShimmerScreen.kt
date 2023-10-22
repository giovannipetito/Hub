package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.MainActivity
import it.giovanni.hub.ui.items.AnimatedShimmer

@Composable
fun AnimatedShimmerScreen(navController: NavController, mainActivity: MainActivity) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            repeat(8) {
                AnimatedShimmer()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedShimmerScreenPreview() {
    AnimatedShimmerScreen(navController = rememberNavController(), mainActivity = MainActivity())
}