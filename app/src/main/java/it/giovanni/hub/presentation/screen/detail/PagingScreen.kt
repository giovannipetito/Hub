package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Character
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.presentation.viewmodel.PagingViewModel
import it.giovanni.hub.presentation.viewmodel.UIEvent
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.HubProgressIndicator
import it.giovanni.hub.ui.items.cards.CharacterCard
import it.giovanni.hub.ui.items.cards.ErrorCard
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Globals.getContentPadding
import kotlinx.coroutines.launch

@Composable
fun PagingScreen(
    navController: NavController,
    viewModel: PagingViewModel = hiltViewModel()
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.paging_3),
    topics = listOf(
        "hiltViewModel",
        "DataSource",
        "suspend functions",
        "sealed class HubResult",
        "Flow",
        "PagingData"
    )
) { paddingValues ->
    val alertBarState: AlertBarState = rememberAlertBarState()
    val scope = rememberCoroutineScope()
    DisposableEffect(Unit) {
        val job = scope.launch {
            viewModel.uiEvents.collect { event ->
                when (event) {
                    is UIEvent.ShowSuccess -> alertBarState.addSuccess(event.message)
                    is UIEvent.ShowError   -> alertBarState.addError(Exception(event.message))
                }
            }
        }
        onDispose { job.cancel() }
    }
    
    val characters: LazyPagingItems<Character> = viewModel.charactersFlow.collectAsLazyPagingItems()

    AlertBarContent(
        position = AlertBarPosition.BOTTOM,
        alertBarState = alertBarState,
        successMaxLines = 3,
        errorMaxLines = 3
    ) {
        ShowCharacters(characters = characters, paddingValues = paddingValues)
    }
}

@Composable
fun ShowCharacters(characters: LazyPagingItems<Character>, paddingValues: PaddingValues) {
    
    LazyColumn(
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        if (characters.itemCount == 0) {
            item {
                HubProgressIndicator()
            }
        }

        items(
            count = characters.itemCount,
            key = characters.itemKey { it.id },
            contentType = characters.itemContentType { "contentType" }
        ) { index: Int ->
            val character: Character? = characters[index]
            Spacer(modifier = Modifier.height(height = 4.dp))
            CharacterCard(character = character!!, modifier = Modifier)
            Spacer(modifier = Modifier.height(height = 4.dp))
        }

        // Handle loading state
        when (val loadState: LoadState = characters.loadState.append) { // .refresh: show loading at the beginning of the list.
            is LoadState.Loading -> {
                item {
                    // Show loading at the end of the list (append).
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        HubProgressIndicator()
                    }
                }
            }
            is LoadState.Error -> {
                item {
                    // Show error item at the end of the list (append).
                    ErrorCard(
                        errorMessage = loadState.error.localizedMessage ?: "Unknown error",
                        onRetry = { characters.retry() }
                    )
                }
            }
            else -> {
                // Do nothing
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagingScreenPreview() {
    PagingScreen(navController = rememberNavController())
}