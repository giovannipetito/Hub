package it.giovanni.hub.presentation.screen.detail.gemini

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    val topics: List<String> = listOf("Generate text from text-and-image input (multimodal)")

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val viewModel: MultimodalViewModel = viewModel()

    var prompt: String by remember { mutableStateOf("") }

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

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.multimodal),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
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
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
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
                    }
                )
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = {
                        if (images.size == 1) {
                            val image = images.first()
                            if (isStreaming) {
                                viewModel.generateContentImageStream(prompt, image)
                            } else {
                                viewModel.generateContentImage(prompt, image)
                            }
                        } else if (images.size > 1) {
                            if (isStreaming) {
                                viewModel.generateContentImagesStream(prompt, images)
                            } else {
                                viewModel.generateContentImages(prompt, images)
                            }
                        }
                    },
                    enabled = prompt.isNotEmpty() && images.isNotEmpty(),
                ) {
                    Text("Generate content")
                }
            }

            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = viewModel.responseText.value ?: ""
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