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
import it.giovanni.hub.navigation.routes.ComfyUIRoutes
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.ui.items.buttons.MainTextButton
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun ComfyUIScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "Text To Image API",
        "Image To Image API"
    )

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.comfy_ui),
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
                        navController.navigate(route = ComfyUIRoutes.TextToImage)
                    },
                    id = R.string.text_to_image
                )
            }
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = ComfyUIRoutes.TextToImageHistory)
                    },
                    id = R.string.text_to_image_history
                )
            }
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = ComfyUIRoutes.HairColor)
                    },
                    id = R.string.hair_color
                )
            }
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = ComfyUIRoutes.HairColorHistory)
                    },
                    id = R.string.hair_color_history
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComfyUIScreenPreview() {
    ComfyUIScreen(navController = rememberNavController())
}