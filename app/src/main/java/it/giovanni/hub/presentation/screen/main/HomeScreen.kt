package it.giovanni.hub.presentation.screen.main

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import it.giovanni.hub.R
import it.giovanni.hub.data.datasource.local.DataStoreRepository
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.utils.Globals.parseUriString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = DataStoreRepository(context)

    val darkThemeFlow: Flow<Boolean> = repository.isDarkTheme()
    val darkTheme: Boolean = remember { darkThemeFlow }.collectAsState(initial = false).value

    val composition: LottieComposition? by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            if (darkTheme) R.raw.dark_theme_welcome
            else R.raw.light_theme_welcome
        )
    )
    var isPlaying: Boolean by rememberSaveable { mutableStateOf(true) }
    val progress: Float by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying
    )
    LaunchedEffect(key1 = progress) {
        if (progress == 0f) {
            isPlaying = true
        }
        if (progress == 1f) {
            isPlaying = false
        }
    }

    var imageUri: Uri? by remember { mutableStateOf(null) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                imageUri = uri
                scope.launch(Dispatchers.IO) {
                    repository.saveUriString(uriString = imageUri.toString())
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    )

    val uriString: String by repository.getUriString().collectAsState(initial = "")

    val avatar: Any? = if (uriString.isEmpty()) {
        if (imageUri == null) {
            if (mainViewModel.getSignedInUser()?.photoUrl == null)
                R.drawable.logo_audioslave
            else
                mainViewModel.getSignedInUser()?.photoUrl
        }
        else
            imageUri
    } else
        parseUriString(uriString)

    /*
    val asyncAvatar: AsyncImagePainter = rememberAsyncImagePainter(model = avatar)

    val bitmap: Bitmap? = if (uriString.isEmpty()) {
        if (imageUri == null)
            BitmapFactory.decodeResource(context.resources, R.drawable.logo_audioslave)
        else
            getBitmapFromUri(context, imageUri!!)
    } else {
        val parsedUri = parseUriString(uriString)
        getBitmapFromUri(context, parsedUri)
    }

    val asyncBitmap: AsyncImagePainter = rememberAsyncImagePainter(model = bitmap)
    */

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            isPlaying = true
                        }
                    ),
                composition = composition,
                progress = { progress }
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(size = 144.dp)
                        .clip(shape = CircleShape)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.outline,
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
                        .data(avatar)
                        .crossfade(enable = true)
                        .build(),
                    contentDescription = "Circular AsyncImage",
                    contentScale = ContentScale.Crop
                )

                /*
                Image(
                    painter = asyncAvatar, // asyncBitmap
                    modifier = Modifier
                        .size(size = 144.dp)
                        .clip(shape = CircleShape)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        ),
                    contentDescription = "Circular Image",
                    contentScale = ContentScale.Crop
                )
                */

                val displayName: String? = mainViewModel.getSignedInUser()?.displayName

                if (displayName != null) {
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = displayName,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}