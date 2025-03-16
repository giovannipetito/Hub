package it.giovanni.hub.ui.items.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.giovanni.hub.utils.Constants

@Composable
fun ScrollableCard() {
    Box(
        modifier = Modifier
            .width(width = 250.dp)
            .height(height = 250.dp)
            .padding(all = 12.dp)
            .clip(shape = RoundedCornerShape(size = 6.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .background(color = MaterialTheme.colorScheme.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .width(width = 200.dp)
            .height(height = 200.dp)
            .clip(shape = RoundedCornerShape(size = 6.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .background(color = MaterialTheme.colorScheme.tertiary)
            .padding(all = 6.dp)
            .verticalScroll(state = rememberScrollState()),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = Constants.LOREM_IPSUM_LONG_TEXT,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScrollableCardPreview() {
    ScrollableCard()
}