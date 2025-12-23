package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.navigation.routes.ComfyUIRoutes
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.presentation.viewmodel.comfyui.ComfyUIViewModel
import it.giovanni.hub.ui.items.buttons.MainTextButton
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun ComfyUIScreen(
    navController: NavController,
    comfyUIViewModel: ComfyUIViewModel
) {
    val topics: List<String> = listOf(
        "Text To Image API",
        "Image To Image API"
    )

    val baseUrl by comfyUIViewModel.baseUrl.collectAsState()
    var editedUrl by remember { mutableStateOf("") }

    LaunchedEffect(baseUrl) {
        editedUrl = baseUrl
    }

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
                OutlinedTextField(
                    value = editedUrl,
                    onValueChange = { editedUrl = it },
                    label = { Text("ComfyUI baseUrl") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        comfyUIViewModel.setBaseUrl(baseUrl = editedUrl)
                    },
                    enabled = editedUrl.isNotBlank()
                ) {
                    Text("Save ComfyUI baseUrl")
                }
            }
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
                        navController.navigate(route = ComfyUIRoutes.HairColor)
                    },
                    id = R.string.hair_color
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComfyUIScreenPreview() {
    ComfyUIScreen(navController = rememberNavController(), comfyUIViewModel = hiltViewModel())
}