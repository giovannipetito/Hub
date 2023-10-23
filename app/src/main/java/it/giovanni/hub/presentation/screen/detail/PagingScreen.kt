package it.giovanni.hub.presentation.screen.detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Character
import androidx.paging.compose.items
import it.giovanni.hub.ui.items.Card2

@Composable
fun PagingScreen(navController: NavController, mainActivity: MainActivity) {

    val characters: LazyPagingItems<Character> = mainActivity.viewModel.getDataFlow().collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue_200)),
        contentAlignment = Alignment.Center,
    ) {
        ShowCharacters(characters)
    }
}

@Composable
fun ShowCharacters(items: LazyPagingItems<Character>) {

    Log.d("[PAGING]", "Load State:" + items.loadState.toString())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        if (items.itemCount == 0) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
        }
        items(
            items = items,
            key = { character ->
                character.id
            }
        ) { character ->
            character?.let { Card2(character = it) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagingScreenPreview() {
    PagingScreen(navController = rememberNavController(), mainActivity = MainActivity())
}