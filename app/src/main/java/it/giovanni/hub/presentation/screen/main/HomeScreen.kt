package it.giovanni.hub.presentation.screen.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.giovanni.hub.R
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.utils.Globals.getBitmapFromUri
import it.giovanni.hub.utils.Globals.parseImageUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = DataStoreRepository(context)

    val imageUri: MutableState<Uri?> = remember { mutableStateOf<Uri?>(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                imageUri.value = uri
                scope.launch(Dispatchers.IO) {
                    repository.saveImageUri(imageUri = imageUri.value.toString())
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    )

    val savedImageUri: State<String> = repository.getImageUri().collectAsState(initial = "")

    val returnedImageUri: Any? = if (savedImageUri.value.isEmpty()) {
        if (imageUri.value == null)
            R.drawable.logo_audioslave
        else
            imageUri.value
    } else
        parseImageUri(savedImageUri.value)

    val bitmap: Bitmap? = if (imageUri.value == null)
        BitmapFactory.decodeResource(context.resources, R.drawable.logo_audioslave)
    else
        getBitmapFromUri(context, imageUri.value!!)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(144.dp)
                    .clip(CircleShape)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clickable {
                        photoPicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                model = ImageRequest.Builder(context)
                    .data(returnedImageUri)
                    .crossfade(enable = true)
                    .build(),
                contentDescription = "Circular AsyncImage",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}