package it.giovanni.hub.presentation.screen.detail.comfyui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.giovanni.hub.R
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.presentation.model.ColorItem
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.presentation.viewmodel.comfyui.ComfyUIViewModel
import it.giovanni.hub.presentation.viewmodel.comfyui.HairColorViewModel
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Globals
import it.giovanni.hub.utils.Globals.getContentPadding
import java.io.File

@Composable
fun HairColorScreen(
    navController: NavController,
    comfyUIViewModel: ComfyUIViewModel,
    viewModel: HairColorViewModel = hiltViewModel()
) {
    val topics: List<String> = listOf(
        "Image To Image API",
        "Camera Permission",
        "Gallery Picker",
        "Camera Picker"
    )

    val context = LocalContext.current

    val alertBarState: AlertBarState = rememberAlertBarState()

    val baseUrl by comfyUIViewModel.baseUrl.collectAsState()

    var prompt by remember { mutableStateOf("") }

    // Resulting image URL
    val imageUrl = viewModel.imageUrl

    // Automatically save when the image arrives
    LaunchedEffect(imageUrl) {
        if (imageUrl != null) {
            viewModel.saveImageToGallery()
        }
    }

    val imageUris = remember { mutableStateListOf<Uri>() }

    /**
     * Gallery picker
     */
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                uri.let { imageUris.add(it) }
            } else {
                Log.d("ComfyUI", "No media selected")
            }
        }
    )

    /**
     * Camera picker
     */
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    fun createImageUri(): Uri? {
        return try {
            val imagesDir = File(context.cacheDir, "images").apply {
                if (!exists()) mkdirs()
            }
            val file = File(
                imagesDir,
                "photo_${System.currentTimeMillis()}.jpg"
            )
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Camera launcher
     */
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri.value?.let { uri ->
                imageUris.add(uri)
            }
        }
    }

    /**
     * Camera permission request
     */
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, open camera
            val uri = createImageUri()
            if (uri != null) {
                cameraImageUri.value = uri
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Cannot create image file", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Camera permission denied. Enable it in Settings to take photos.", Toast.LENGTH_LONG).show()
        }
    }

    val colorItems = Globals.getColorItems()

    var selectedColorItem by remember { mutableStateOf<ColorItem?>(null) }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.hair_color),
        topics = topics
    ) { paddingValues ->

        AlertBarContent(
            position = AlertBarPosition.BOTTOM,
            alertBarState = alertBarState,
            successMaxLines = 3,
            errorMaxLines = 3
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = getContentPadding(paddingValues = paddingValues)
            ) {
                items(
                    items = imageUris
                ) { imageUri ->
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 24.dp)
                            .clip(shape = RoundedCornerShape(size = 12.dp)),
                        model = ImageRequest.Builder(context)
                            .data(imageUri)
                            .crossfade(enable = true)
                            .build(),
                        contentDescription = "Rounded Corner AsyncImage",
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(height = 4.dp))
                }

                if (imageUris.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    val hasCameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                    if (hasCameraPermission) {
                                        // Permission granted, open camera
                                        val uri = createImageUri()
                                        if (uri != null) {
                                            cameraImageUri.value = uri
                                            cameraLauncher.launch(uri)
                                        } else {
                                            Toast.makeText(context, "Cannot create image file", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        // Ask for permission
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }
                            ) {
                                Text("Take photo with camera")
                            }

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    photoPicker.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                            ) {
                                Text("Pick photo from gallery")
                            }
                        }
                    }
                } else {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(Color.DarkGray.copy(alpha = 0.1f))
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                items(colorItems) { item ->

                                    val isSelected = selectedColorItem == item

                                    val animatedScale by animateFloatAsState(
                                        targetValue = if (isSelected) 1.1f else 1f,
                                        label = "itemScale"
                                    )

                                    val animatedColor by animateColorAsState(
                                        targetValue = if (isSelected) {
                                            item.color
                                        } else {
                                            item.color.copy(alpha = 0.6f)
                                        },
                                        label = "itemColor"
                                    )

                                    Box(
                                        modifier = Modifier
                                            .graphicsLayer {
                                                scaleX = animatedScale
                                                scaleY = animatedScale
                                            }
                                            .width(60.dp)
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(animatedColor)
                                            .clickable {
                                                selectedColorItem = item
                                            }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            selectedColorItem?.let { selected ->
                                Button(
                                    onClick = {
                                        val sourceImageUri = imageUris.firstOrNull()
                                        if (sourceImageUri == null) {
                                            Toast.makeText(context, "Pick or take a photo first", Toast.LENGTH_SHORT).show()
                                        } else {
                                            prompt = "${selected.name} hair"
                                            viewModel.generateImage(baseUrl = baseUrl, prompt = prompt, sourceImageUri = sourceImageUri) { result: Result<Unit> ->
                                                result
                                                    .onSuccess { alertBarState.addSuccess("Generation successful!") }
                                                    .onFailure { alertBarState.addError(it) }
                                            }
                                        }
                                    },
                                    enabled = selected.name.isNotBlank()
                                ) {
                                    Text(text = "Dye your hair ${selected.name}")
                                }
                            }
                        }
                    }
                }

                item {
                    if (imageUrl != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 24.dp)
                                    .clip(shape = RoundedCornerShape(size = 12.dp)),
                                model = ImageRequest.Builder(context)
                                    .data(imageUrl)
                                    .crossfade(enable = true)
                                    .build(),
                                contentDescription = "Dyed hair result",
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(height = 4.dp))
                        }
                    }
                }
            }
        }
    }
}