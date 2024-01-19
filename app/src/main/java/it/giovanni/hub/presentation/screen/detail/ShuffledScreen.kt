package it.giovanni.hub.presentation.screen.detail

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.Text2
import it.giovanni.hub.ui.items.buttons.HubButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShuffledScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.shuffled_items),
    topics = listOf("animateItemPlacement", "shuffled")
) {
    var languages: List<String> by remember {
        mutableStateOf(listOf("Kotlin", "Java", "Python", "Swift", "JavaScript", "Dart"))
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = languages,
            key = { it }
        ) { item: String ->
            Text2(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(durationMillis = 600)
                ),
                text = item,
                textColor = MaterialTheme.colorScheme.primary
            )
        }
        item {
            HubButton(
                modifier = Modifier,
                text = "Shuffle",
                onClick = {
                    languages = languages.shuffled()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShuffledScreenPreview() {
    ShuffledScreen(navController = rememberNavController())
}