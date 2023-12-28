package it.giovanni.hub.ui.items.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun HubCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(all = 12.dp)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Card"
        )
    }
}

@Preview
@Composable
fun HubFilledCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(all = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Filled Card"
        )
    }
}

@Preview
@Composable
fun HubElevatedCard() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(all = 12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Elevated Card"
        )
    }
}

@Preview
@Composable
fun HubOutlinedCard() {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(all = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Outlined Card"
        )
    }
}