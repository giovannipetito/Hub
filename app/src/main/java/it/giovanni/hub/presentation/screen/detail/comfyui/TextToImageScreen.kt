package it.giovanni.hub.presentation.screen.detail.comfyui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import it.giovanni.hub.R
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.presentation.viewmodel.comfyui.TextToImageViewModel
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun TextToImageScreen(
    navController: NavController,
    viewModel: TextToImageViewModel = hiltViewModel()
) {
    val topics: List<String> = listOf("Text To Image API")

    val context = LocalContext.current
    val state: AlertBarState = rememberAlertBarState()

    val baseUrl by viewModel.comfyUrl.collectAsState()
    var editedUrl by remember { mutableStateOf("") }

    var prompt by remember { mutableStateOf("") }
    var autoSave by rememberSaveable { mutableStateOf(true) }
    val imageUrl = viewModel.imageUrl
    val saveResult by viewModel.saveResult.collectAsState(initial = null)

    LaunchedEffect(baseUrl) {
        editedUrl = baseUrl
    }

    // Show a toast every time the ViewModel tells us the save finished
    saveResult?.let { success ->
        LaunchedEffect(saveResult) {
            Toast.makeText(
                context,
                if (success) "Saved to gallery" else "Could not save image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Automatically save when the image arrives and the toggle is ON
    LaunchedEffect(imageUrl, autoSave) {
        if (imageUrl != null && autoSave) {
            viewModel.saveImageToGallery()
        }
    }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.text_to_image),
        topics = topics
    ) { paddingValues ->

        AlertBarContent(
            position = AlertBarPosition.BOTTOM,
            alertBarState = state,
            successMaxLines = 3,
            errorMaxLines = 3
        ) {
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
                        label = { Text("Comfy baseUrl") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.setBaseUrl(baseUrl = editedUrl) }) {
                        Text("Save Comfy baseUrl")
                    }
                }

                item {
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = { Text("Enter prompt") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.generateImage(prompt = prompt) { result: Result<Unit> ->
                                result
                                    .onSuccess { state.addSuccess("Generation successful!") }
                                    .onFailure { state.addError(it) }
                            }
                        },
                        enabled = prompt.isNotBlank()
                    ) {
                        Text("Generate Image")
                    }
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(checked = autoSave, onCheckedChange = { autoSave = it })
                        Spacer(Modifier.width(8.dp))
                        Text(if (autoSave) "Auto-save ON" else "Auto-save OFF")
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    viewModel.imageUrl?.let { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Generated image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )
                    }
                }

                item {
                    if (!autoSave && imageUrl != null) {
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = viewModel::saveImageToGallery) {
                            Icon(
                                modifier = Modifier.size(size = 18.dp),
                                painter = painterResource(id = R.drawable.ico_save),
                                contentDescription = "Save Icon",
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Save to gallery")
                        }
                    }
                }
            }
        }
    }
}