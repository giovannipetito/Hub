package it.giovanni.hub.presentation.screen.detail

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.GeminiViewModel
import it.giovanni.hub.ui.items.SimpleSwitch
import it.giovanni.hub.utils.Globals.decodeUriToBitmap
import it.giovanni.hub.utils.Globals.getContentPadding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GeminiScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "Generate content from text-only input and text-and-image input (multimodal) using Streaming for faster interactions"
    )

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val viewModel: GeminiViewModel = viewModel()

    var prompt: String by remember { mutableStateOf("") }

    var isStreaming by remember { mutableStateOf(false) }

    val bitmaps = remember { mutableStateListOf<Bitmap>() }

    val multimodalItems = remember { mutableStateListOf<MultimodalItem>() }

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 3),
        onResult = { uris ->
            uris.forEach { uri ->
                coroutineScope.launch(Dispatchers.IO) {
                    decodeUriToBitmap(context, uri).let { bitmap ->
                        if (bitmap != null) {
                            bitmaps.add(bitmap)
                            multimodalItems.add(MultimodalItem.BitmapItem(uri, bitmap))
                        }
                    }
                }
            }
        }
    )

    fun displayContentResponse(success: Boolean) {
        if (success) {
            multimodalItems.add(MultimodalItem.PromptItem(prompt = prompt))
            prompt = ""
            multimodalItems.add(MultimodalItem.ContentItem(viewModel.contentResponse.value ?: ""))
        } else {
            multimodalItems.add(MultimodalItem.ErrorItem("An error occurred: " + viewModel.contentResponse.value))
        }
    }

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
                            items = multimodalItems
                        ) { multimodalItem ->
                            when (multimodalItem) {
                                is MultimodalItem.PromptItem -> {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = multimodalItem.prompt,
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                is MultimodalItem.BitmapItem -> {
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
                                            .data(multimodalItem.uri)
                                            .crossfade(enable = true)
                                            .build(),
                                        contentDescription = "Rounded Corner AsyncImage",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                is MultimodalItem.ContentItem -> {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = multimodalItem.content,
                                        textAlign = TextAlign.Justify,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                is MultimodalItem.ErrorItem -> {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = multimodalItem.error,
                                        textAlign = TextAlign.Center,
                                        color = Color.Red
                                    )
                                }
                            }
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
                                modifier = Modifier.size(size = 24.dp),
                                painter = painterResource(id = R.drawable.ico_add),
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.secondary),
                                contentDescription = "Add Icon"
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (bitmaps.isEmpty()) {
                                    if (isStreaming) {
                                        viewModel.generateContentTextStream(prompt) { success ->
                                            displayContentResponse(success)
                                        }
                                    } else {
                                        viewModel.generateContentText(prompt) { success ->
                                            displayContentResponse(success)
                                        }
                                    }
                                } else if (bitmaps.size == 1) {
                                    val image = bitmaps.first()
                                    if (isStreaming) {
                                        viewModel.generateContentImageStream(prompt, image) { success ->
                                            displayContentResponse(success)
                                        }
                                    } else {
                                        viewModel.generateContentImage(prompt, image) { success ->
                                            displayContentResponse(success)
                                        }
                                    }
                                } else if (bitmaps.size > 1) {
                                    if (isStreaming) {
                                        viewModel.generateContentImagesStream(prompt, bitmaps) { success ->
                                            displayContentResponse(success)
                                        }
                                    } else {
                                        viewModel.generateContentImages(prompt, bitmaps) { success ->
                                            displayContentResponse(success)
                                        }
                                    }
                                }
                            },
                            enabled = prompt.isNotEmpty()
                        ) {
                            Image(
                                modifier = Modifier.size(size = 24.dp),
                                painter = painterResource(id = R.drawable.ico_done),
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

sealed class MultimodalItem {
    data class PromptItem(val prompt: String) : MultimodalItem()
    data class BitmapItem(val uri: Uri, val bitmap: Bitmap) : MultimodalItem()
    data class ContentItem(val content: String) : MultimodalItem()
    data class ErrorItem(val error: String) : MultimodalItem()
}

@Preview(showBackground = true)
@Composable
fun GeminiScreenPreview() {
    GeminiScreen(navController = rememberNavController())
}