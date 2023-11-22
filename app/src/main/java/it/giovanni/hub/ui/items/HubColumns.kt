package it.giovanni.hub.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
fun Column1(
    alignment: Alignment.Horizontal,
    arrangement: Arrangement.HorizontalOrVertical
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = alignment,
        verticalArrangement = arrangement
    ) {
        ColumnItem1(color = MaterialTheme.colorScheme.primary)
        ColumnItem1(color = MaterialTheme.colorScheme.secondary)
        ColumnItem1(color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
fun Column2(
    alignment: Alignment.Horizontal,
    arrangement: Arrangement.HorizontalOrVertical
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan),
        horizontalAlignment = alignment,
        verticalArrangement = arrangement
    ) {
        ColumnItem2(weight = 1f)
        ColumnItem2(weight = 2f, color = MaterialTheme.colorScheme.secondary)
        ColumnItem2(weight = 3f, color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
fun ColumnScope.ColumnItem1(color: Color) {
    Surface(
        modifier = Modifier
            .width(200.dp)
            .height(50.dp)
            .border(
                width = 1.dp,
                color = Color.Red
            ),
        color = color
    ) {}
}

@Composable
fun ColumnScope.ColumnItem2(weight: Float, color: Color = MaterialTheme.colorScheme.primary) {
    Surface(
        modifier = Modifier
            .width(200.dp)
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
fun Column1Preview() {
    Column1(
        alignment = Alignment.CenterHorizontally,
        arrangement = Arrangement.Center
    )
}

@Preview(showBackground = true)
@Composable
fun Column2Preview() {
    Column2(
        alignment = Alignment.CenterHorizontally,
        arrangement = Arrangement.Center
    )
}