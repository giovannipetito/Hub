package it.giovanni.hub.presentation.screen.detail

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import it.giovanni.hub.presentation.viewmodel.PagingViewModel
import it.giovanni.hub.ui.items.HubCircularProgressIndicator

@Composable
fun PagingScreen(
    navController: NavController,
    viewModel: PagingViewModel = hiltViewModel()
) {
    val topics: List<String> = listOf(
        "hiltViewModel",
        "DataSource",
        "suspend functions",
        "sealed class HubResult",
        "Flow",
        "PagingData"
    )

    val characters: LazyPagingItems<Character> = viewModel.getDataFlow().collectAsLazyPagingItems()

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.paging_3),
        topics = topics
    ) { paddingValues ->
        ShowCharacters(characters = characters, paddingValues = paddingValues)
    }
}

@Composable
fun ShowCharacters(characters: LazyPagingItems<Character>, paddingValues: PaddingValues) {

    Log.d("[PAGING]", "Load State:" + characters.loadState.toString())

    LazyColumn(
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = paddingValues.calculateBottomPadding())
    ) {
        if (characters.itemCount == 0) {
            item {
                HubCircularProgressIndicator()
            }
        }
        /*
        items(items = characters) { character ->
            character?.let {
                CharacterCard(character = it, modifier = Modifier)
            }
        }
        */

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