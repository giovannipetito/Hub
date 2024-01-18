package it.giovanni.hub.presentation.screen.detail.topappbars

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.ui.items.LazyColumn2
import it.giovanni.hub.utils.Globals.isScrolled

@Composable
fun CollapsingTopAppBarScreen(navController: NavController) {

    val lazyListState: LazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            CollapsingTopAppBar(lazyListState = lazyListState)
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn2(lazyListState = lazyListState, paddingValues = it)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingTopAppBar(lazyListState: LazyListState) {
    TopAppBar(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .animateContentSize(animationSpec = tween(durationMillis = 400))
            .height(height = if (lazyListState.isScrolled) 0.dp else (90.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Collapsing TopAppBar"
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CollapsingTopAppBarScreenPreview() {
    CollapsingTopAppBarScreen(navController = rememberNavController())
}