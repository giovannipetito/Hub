package it.giovanni.hub.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Column1() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Surface(
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            color = MaterialTheme.colorScheme.primary
        ) {}
        Surface(
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            color = MaterialTheme.colorScheme.primary
        ) {}
        Surface(
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            color = MaterialTheme.colorScheme.primary
        ) {}
        Surface(
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
            color = MaterialTheme.colorScheme.primary
        ) {}
    }
}

@Composable
fun Column2() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .width(200.dp)
                .weight(3f),
            color = MaterialTheme.colorScheme.primary
        ) {}
        Surface(
            modifier = Modifier
                .width(200.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.secondary
        ) {}
    }
}

@Composable
fun Column3() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomItem(weight = 4f)
        CustomItem(weight = 4f, color = MaterialTheme.colorScheme.secondary)
        CustomItem(weight = 4f, color = MaterialTheme.colorScheme.tertiary)
    }
}