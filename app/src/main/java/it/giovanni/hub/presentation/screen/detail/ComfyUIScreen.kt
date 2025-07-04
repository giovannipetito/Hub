package it.giovanni.hub.presentation.screen.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.ComfyUIViewModel
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun ComfyUIScreen(
    navController: NavController,
    viewModel: ComfyUIViewModel = hiltViewModel()
) {
    val topics: List<String> = listOf("")

    val ctx = LocalContext.current
    var promptText by remember { mutableStateOf("") }
    var autoSave by rememberSaveable { mutableStateOf(true) }
    val imageUrl = viewModel.imageUrl
    val saveResult by viewModel.saveResult.collectAsState(initial = null)

    //  Show a toast every time the ViewModel tells us the save finished
    saveResult?.let { success ->
        LaunchedEffect(saveResult) {
            Toast.makeText(
                ctx,
                if (success) "Saved to gallery" else "Could not save image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /** Automatically save when the image arrives and the toggle is ON */
    LaunchedEffect(imageUrl, autoSave) {
        if (imageUrl != null && autoSave) {
            viewModel.saveImageToGallery()
        }
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
                    value = promptText,
                    onValueChange = { promptText = it },
                    label = { Text("Enter prompt") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.generateImage(promptText = promptText) }) {
                    Text("Generate Image")
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
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
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Save to gallery")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComfyUIScreenPreview() {
    ComfyUIScreen(navController = rememberNavController())
}