package it.giovanni.hub.presentation.screen.main

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
import it.giovanni.hub.navigation.util.routes.GeminiRoutes
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.ui.items.buttons.MainTextButton
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun GeminiScreen(navController: NavController) {

    val topics: List<String> = listOf("Gemini")

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.gemini),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = GeminiRoutes.TextInput)
                    },
                    id = R.string.text_input
                )
            }
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = GeminiRoutes.Multimodal)
                    },
                    id = R.string.multimodal
                )
            }
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = GeminiRoutes.Chat)
                    },
                    id = R.string.chat
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GeminiScreenPreview() {
    GeminiScreen(navController = rememberNavController())
}