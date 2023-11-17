package it.giovanni.hub.ui.items.cards

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.giovanni.hub.ui.items.previews.DevicePreviews
import it.giovanni.hub.ui.items.previews.FontScalesPreviews

@Composable
fun PreviewsCard() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(size = 12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello, World!",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize)
            )
            Text(
                text = "Giovanni Petito Hub",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize)
            )
        }
    }
}

@Composable
@FontScalesPreviews
@DevicePreviews
fun PreviewsCardPreview() {
    PreviewsCard()
}