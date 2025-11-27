package it.giovanni.hub.presentation.screen.detail

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.giovanni.hub.R
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun PhotoPickerScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "PickMultipleVisualMedia",
        "rememberLauncherForActivityResult",
        "AsyncImage",
        "RoundedCornerShape"
    )

    val context = LocalContext.current

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.photo_picker),
        topics = topics
    ) { paddingValues ->

        Toast.makeText(
            context,
            "Is Photo picker available? " + ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(context).toString(),
            Toast.LENGTH_SHORT
        ).show()

        val imageUris = remember { mutableStateListOf<Uri>() }

        val multiplePhotoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 2),
            onResult = {
                imageUris.addAll(it)
            }
        )

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
                Spacer(modifier = Modifier.height(height = 4.dp))
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = {
                        multiplePhotoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Text("Pick photos")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoPickerPreview() {
    PhotoPickerScreen(navController = rememberNavController())
}