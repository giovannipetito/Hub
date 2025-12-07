package it.giovanni.hub.presentation.screen.detail

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.SpeechToTextViewModel
import it.giovanni.hub.ui.items.microphoneIcon
import it.giovanni.hub.utils.Globals.getContentPadding
import it.giovanni.hub.utils.Globals.getTransitionColor

@Composable
fun SpeechToTextScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.speech_to_text),
    topics = listOf("RecognitionListener", "RecognizerIntent", "SpeechRecognizer")
) { paddingValues ->

    val viewModel: SpeechToTextViewModel = viewModel()

    val context = LocalContext.current
    val speechText by viewModel.speechText.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val showRationale by viewModel.showRationale.collectAsState()
    val permissionDenied by viewModel.permissionDenied.collectAsState()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.startListening(context)
        } else {
            viewModel.setPermissionDenied(true)
        }
    }

    if (showRationale) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowRationale(false) },
            title = { Text(text = "Permission Required") },
            text = { Text(text = "We need access to your microphone to recognize speech.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setShowRationale(false)
                        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                ) {
                    Text("Allow")
                }
            },
            dismissButton = {
                Button(
                    onClick = { viewModel.setShowRationale(false) }
                ) {
                    Text("Deny")
                }
            }
        )
    }

    if (permissionDenied) {
        AlertDialog(
            onDismissRequest = { viewModel.setPermissionDenied(false) },
            title = { Text(text = "Permission Denied") },
            text = { Text(text = "You have denied the microphone permission. The app cannot recognize speech without it.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.setPermissionDenied(false) }
                ) {
                    Text("Ok")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        item {
            IconButton(
                modifier = Modifier
                    .size(size = 96.dp)
                    .clip(shape = CircleShape)
                    .border(
                        width = 4.dp,
                        color = if (isListening) getTransitionColor(durationMillis = 400) else MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    ),
                onClick = {
                    when {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            viewModel.startListening(context)
                        }
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            context as Activity,
                            Manifest.permission.RECORD_AUDIO
                        ) -> {
                            viewModel.setShowRationale(true)
                        }
                        else -> {
                            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(size = 48.dp),
                    painter = microphoneIcon(),
                    contentDescription = "Microphone Icon",
                    tint = if (isListening) getTransitionColor(durationMillis = 400) else MaterialTheme.colorScheme.primary
                )
            }
        }

        item {
            Text(
                text = speechText,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpeechToTextScreenPreview() {
    SpeechToTextScreen(navController = rememberNavController())
}