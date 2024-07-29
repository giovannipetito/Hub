package it.giovanni.hub.presentation.screen.detail

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import it.giovanni.hub.R
import it.giovanni.hub.data.model.SignedInUser
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.presentation.viewmodel.NetworkViewModel
import it.giovanni.hub.ui.items.FullScreenProgressIndicator
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun NetworkScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    viewModel: NetworkViewModel = hiltViewModel()
) {
    val topics: List<String> = listOf(
        "Worker", "WorkManager", "Python", "Flask Server", "Notification"
    )
    val context = LocalContext.current

    var message: String by remember { mutableStateOf("") }

    val signedInUser: SignedInUser? = mainViewModel.getSignedInUser()

    val showRationale by viewModel.showRationale.collectAsState()
    val permissionDenied by viewModel.permissionDenied.collectAsState()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            viewModel.setPermissionDenied(true)
        }
    }

    var isClicked: Boolean by remember { mutableStateOf(false) }

    if (showRationale) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowRationale(false) },
            title = { Text(text = "Permission Required") },
            text = { Text(text = "We need access to your permission to send notifications.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setShowRationale(false)
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
            text = { Text(text = "You have denied the notification permission. The app cannot receive notifications.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.setPermissionDenied(false) }
                ) {
                    Text("Ok")
                }
            }
        )
    }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.network),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Python") },
                    placeholder = { Text("Send a message to Python") },
                    singleLine = true
                )
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = {
                        viewModel.setUsername(signedInUser?.displayName ?: "")
                        viewModel.setMessage(message)
                        viewModel.sendMessage(context)
                        isClicked = true
                    },
                    enabled = message.isNotEmpty()
                ) {
                    Text("Send message")
                }
            }

            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = viewModel.responseMessage.value
                )
            }
        }

        if (isClicked && viewModel.requestStatus.value == "Running")
            FullScreenProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun NetworkScreenPreview() {
    NetworkScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}