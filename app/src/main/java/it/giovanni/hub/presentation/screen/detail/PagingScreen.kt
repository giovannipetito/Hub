package it.giovanni.hub.presentation.screen.detail

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import androidx.compose.runtime.Composable
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
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.HubCircularProgressIndicator
import it.giovanni.hub.ui.items.cards.CharacterCard
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Globals.getContentPadding

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
    val state: AlertBarState = rememberAlertBarState()
    val characters: LazyPagingItems<Character> = viewModel.getDataFlow(state = state).collectAsLazyPagingItems()

    AlertBarContent(
        position = AlertBarPosition.BOTTOM,
        alertBarState = state,
        successMaxLines = 3,
        errorMaxLines = 3
    ) {
        ShowCharacters(characters = characters, paddingValues = paddingValues)
    }
}

@Composable
fun ShowCharacters(characters: LazyPagingItems<Character>, paddingValues: PaddingValues) {

    Log.d("[PAGING]", "Load State:" + characters.loadState.toString())

    LazyColumn(
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        if (characters.itemCount == 0) {
            item {
                HubCircularProgressIndicator()
            }
        }

        items(
            count = characters.itemCount,
            key = characters.itemKey { it.id },
            contentType = characters.itemContentType { "contentType" }
        ) { index: Int ->
            val character: Character? = characters[index]
            Spacer(modifier = Modifier.height(4.dp))
            CharacterCard(character = character!!, modifier = Modifier)
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Handle loading state // todo: to test.
        characters.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    // Show loading at the beginning of the list
                }
                loadState.append is LoadState.Loading -> {
                    // Show loading at the end of the list
                }
                loadState.refresh is LoadState.Error -> {
                    // Handle error at the beginning of the list
                }
                loadState.append is LoadState.Error -> {
                    // Handle error at the end of the list
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagingScreenPreview() {
    PagingScreen(navController = rememberNavController())
}