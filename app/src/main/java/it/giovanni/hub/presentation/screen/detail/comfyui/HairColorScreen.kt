package it.giovanni.hub.presentation.screen.detail.comfyui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun HairColorScreen(navController: NavController) {

    val topics: List<String> = listOf("")

    val context = LocalContext.current

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.hair_color),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HairColorScreenPreview() {
    HairColorScreen(navController = rememberNavController())
}