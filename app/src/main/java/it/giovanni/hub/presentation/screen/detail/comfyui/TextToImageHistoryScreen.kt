package it.giovanni.hub.presentation.screen.detail.comfyui

import androidx.compose.foundation.clickable
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import it.giovanni.hub.R
import it.giovanni.hub.data.model.comfyui.HistoryItem
import it.giovanni.hub.presentation.viewmodel.ComfyUIViewModel
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun TextToImageHistoryScreen(
    navController: NavController,
    viewModel: ComfyUIViewModel = hiltViewModel()
) {
    val topics: List<String> = listOf("")

    // Kick off the network call the first time this Composable appears
    LaunchedEffect(Unit) { viewModel.getHistory() }

    // Recompose whenever the history list changes
    val history by viewModel.history.collectAsState()

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.text_to_image_history),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues)
        ) {
            items(history, key = { it.id }) { item ->
                HistoryCard(item)
            }
        }
    }
}

@Composable
private fun HistoryCard(item: HistoryItem) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { uriHandler.openUri(item.output.firstOrNull()?.url.orEmpty()) },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.output.firstOrNull()?.thumbnailUrl.orEmpty(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.createdAt.replace('T', ' ').dropLast(1),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextToImageHistoryScreenPreview() {
    TextToImageHistoryScreen(navController = rememberNavController())
}