package it.giovanni.hub.presentation.screen.detail

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.ui.items.Text2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShuffledScreen(navController: NavController) {

    var languages by remember {
        mutableStateOf(
            listOf("Kotlin", "Java", "Python", "Swift", "JavaScript", "Dart")
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = languages,
                key = {it}
            ) { item: String ->
                Text2(
                    text = item,
                    modifier = Modifier
                        .animateItemPlacement(
                            animationSpec = tween(durationMillis = 600)
                        )
                )
            }
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = {
                        languages = languages.shuffled()
                    }
                ) {
                    Text("Shuffle")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShuffledScreenPreview() {
    ShuffledScreen(navController = rememberNavController())
}