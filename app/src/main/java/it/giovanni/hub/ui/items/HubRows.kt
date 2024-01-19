package it.giovanni.hub.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Row1(
    alignment: Alignment.Vertical,
    arrangement: Arrangement.HorizontalOrVertical
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = alignment,
        horizontalArrangement = arrangement
    ) {
        Row1Item(color = MaterialTheme.colorScheme.primary)
        Row1Item(color = MaterialTheme.colorScheme.secondary)
        Row1Item(color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
fun Row2(
    alignment: Alignment.Vertical,
    arrangement: Arrangement.HorizontalOrVertical
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        verticalAlignment = alignment,
        horizontalArrangement = arrangement
    ) {
        Row2Item(weight = 1f)
        Row2Item(weight = 2f, color = MaterialTheme.colorScheme.secondary)
        Row2Item(weight = 3f, color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
fun RowScope.Row1Item(color: Color) {
    Surface(
        modifier = Modifier
            .width(80.dp)
            .height(50.dp)
            .border(
                width = 1.dp,
                color = Color.Red
            ),
        color = color
    ) {}
}

@Composable
fun RowScope.Row2Item(weight: Float, color: Color = MaterialTheme.colorScheme.primary) {
    Surface(
        modifier = Modifier
            .height(80.dp)
            .weight(weight)
            .border(
                width = 1.dp,
                color = Color.Red
            ),
        color = color
    ) {}
}

@Preview(showBackground = true)
@Composable
fun Row1Preview() {
    Row1(
        alignment = Alignment.CenterVertically,
        arrangement = Arrangement.Center
    )
}

@Preview(showBackground = true)
@Composable
fun Row2Preview() {
    Row2(
        alignment = Alignment.CenterVertically,
        arrangement = Arrangement.Center
    )
}
