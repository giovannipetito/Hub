package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.Box1

@Composable
fun HubBoxesScreen(navController: NavController) {

    val topics: List<String> = listOf("Box", "verticalScroll")

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.boxes),
        topics = topics
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                Box1()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubBoxesScreenPreview() {
    HubBoxesScreen(navController = rememberNavController())
}