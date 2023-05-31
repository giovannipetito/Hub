package it.giovanni.hub.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Box1() {
    Box(modifier = Modifier
        .background(Color.Blue)
        .width(150.dp)
        .height(150.dp)
    ) {}
}

@Composable
fun Box2() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .background(Color.Blue)
            .width(150.dp)
            .height(150.dp)
        ) {}
    }
}

@Composable
fun Box3() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .background(Color.Blue)
            .width(150.dp)
            .height(150.dp)
            .verticalScroll(state = rememberScrollState()),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(text = "Welome to Jetpack Compose!", fontSize = 24.sp, color = Color.White)
        }
    }
}