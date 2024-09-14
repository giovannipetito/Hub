package it.giovanni.hub.presentation.screen.detail.gemini

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.giovanni.hub.R
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.presentation.viewmodel.gemini.MultimodalViewModel
import it.giovanni.hub.ui.items.SimpleSwitch
import it.giovanni.hub.utils.Globals.decodeUriToBitmap
import it.giovanni.hub.utils.Globals.getContentPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MultimodalScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "Generate content from text-only input and text-and-image input (multimodal) using Streaming for faster interactions"
    )

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val viewModel: MultimodalViewModel = viewModel()

    var prompt: String by remember { mutableStateOf("") }

    var content: String by remember { mutableStateOf("") }

    var isStreaming by remember { mutableStateOf(false) }

    val imageUris = remember { mutableStateListOf<Uri>() }

    val images = remember { mutableStateListOf<Bitmap>() }

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 3),
        onResult = { uris ->
            imageUris.addAll(uris)
            uris.forEach { uri ->
                coroutineScope.launch(Dispatchers.IO) {
                    decodeUriToBitmap(context, uri).let { image ->
                        if (image != null)
                            images.add(image)
                    }
                }
            }
        }
    )

    LaunchedEffect(key1 = viewModel.contentResponse.value) {
        if (viewModel.contentResponse.value != null && viewModel.contentResponse.value != "") {
            prompt = ""
        }
    }

    fun handleContentResponse(success: Boolean) {
        if (success)
            content += viewModel.contentResponse.value ?: ""
        else
            content = "Error occurred"
    }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.multimodal),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    SimpleSwitch(
                        checked = isStreaming,
                        onCheckedChange = { isStreaming = !isStreaming }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = "Streaming",
                        fontWeight = FontWeight.Bold,
                        color = if (isStreaming)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(all = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(all = 12.dp),
                    ) {
                        items(
                            items = imageUris
                        ) { imageUri ->
                            AsyncImage(
                                modifier = Modifier
                                    .size(size = 144.dp)
                                    .clip(shape = RoundedCornerShape(size = 12.dp))
                                    .border(
                                        width = 4.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(size = 12.dp)
                                    ),
                                model = ImageRequest.Builder(context)
                                    .data(imageUri)
                                    .crossfade(enable = true)
                                    .build(),
                                contentDescription = "Rounded Corner AsyncImage",
                                contentScale = ContentScale.Crop
                            )
                        }

                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = content
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
                    value = prompt,
                    onValueChange = { prompt = it },
                    label = { Text("Prompt") },
                    placeholder = { Text("Ask to Gemini") },
                    singleLine = true,
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                multiplePhotoPicker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        ) {
                            Image(
                                imageVector = Icons.Default.Add,
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary),
                                contentDescription = "Add Icon"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (imageUris.isEmpty()) {
                                    if (isStreaming) {
                                        viewModel.generateContentTextStream(prompt) { success ->
                                            handleContentResponse(success)
                                        }
                                    } else {
                                        viewModel.generateContentText(prompt) { success ->
                                            handleContentResponse(success)
                                        }
                                    }
                                } else if (images.size == 1) {
                                    val image = images.first()
                                    if (isStreaming) {
                                        viewModel.generateContentImageStream(prompt, image) { success ->
                                            handleContentResponse(success)
                                        }
                                    } else {
                                        viewModel.generateContentImage(prompt, image) { success ->
                                            handleContentResponse(success)
                                        }
                                    }
                                } else if (images.size > 1) {
                                    if (isStreaming) {
                                        viewModel.generateContentImagesStream(prompt, images) { success ->
                                            handleContentResponse(success)
                                        }
                                    } else {
                                        viewModel.generateContentImages(prompt, images) { success ->
                                            handleContentResponse(success)
                                        }
                                    }
                                }
                            },
                            enabled = prompt.isNotEmpty()
                        ) {
                            Image(
                                imageVector = Icons.Default.Done,
                                colorFilter =
                                if (prompt.isNotEmpty()) ColorFilter.tint(color = MaterialTheme.colorScheme.secondary)
                                else ColorFilter.tint(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                                contentDescription = "Done Icon"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultimodalScreenPreview() {
    MultimodalScreen(navController = rememberNavController())
}