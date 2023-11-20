package it.giovanni.hub.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
fun Row1() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
        // horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomItem(weight = 4f)
        CustomItem(weight = 4f, color = MaterialTheme.colorScheme.secondary)
        CustomItem(weight = 4f, color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
fun RowScope.CustomItem(weight: Float, color: Color = MaterialTheme.colorScheme.primary) {
    Surface(
        modifier = Modifier
            .width(80.dp)
            .height(50.dp),
        // .weight(weight),
        color = color
    ) {}
}