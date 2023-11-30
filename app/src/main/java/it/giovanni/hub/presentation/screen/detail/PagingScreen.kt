package it.giovanni.hub.presentation.screen.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import it.giovanni.hub.data.model.Character
import it.giovanni.hub.presentation.viewmodel.PagingViewModel
import it.giovanni.hub.ui.items.cards.CharacterCard
// import androidx.paging.compose.items

@Composable
fun PagingScreen(
    navController: NavController,
    viewModel: PagingViewModel = hiltViewModel()
) {
    val characters: LazyPagingItems<Character> = viewModel.getDataFlow().collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        ShowCharacters(characters)
    }
}

@Composable
fun ShowCharacters(characters: LazyPagingItems<Character>) {

    Log.d("[PAGING]", "Load State:" + characters.loadState.toString())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        if (characters.itemCount == 0) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.Center)
                        .fillMaxSize()
                )
            }
        }
        /*
        items(
            items = characters,
            key = { character ->
                character.id
            }
        ) { character ->
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