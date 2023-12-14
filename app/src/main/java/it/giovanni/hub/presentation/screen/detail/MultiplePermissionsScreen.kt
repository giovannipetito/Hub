package it.giovanni.hub.presentation.screen.detail

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.*
import androidx.navigation.NavController

@ExperimentalPermissionsApi
@Composable
fun MultiplePermissionsScreen(navController: NavController) {

    val permissions: List<String> = listOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CAMERA
    )
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    val deniedMessage = "Give this app the permissions to proceed. If it doesn't work, then you'll have to do it manually from the settings."
    val rationaleMessage = "To use this app's functionalities, you need to give us the permission."

    RequestMultiplePermissions(
        multiplePermissionsState = multiplePermissionsState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                deniedMessage = deniedMessage,
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = {
                    multiplePermissionsState.launchMultiplePermissionRequest()
                }
            )
        },
        grantedContent = {
            PermissionGrantedContent(
                navController = navController,
                text = "Permissions Granted!",
                showButton = false
            ) {}
        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun RequestMultiplePermissions(
    multiplePermissionsState: MultiplePermissionsState,
    deniedContent: @Composable (Boolean) -> Unit,
    grantedContent: @Composable () -> Unit
) {
    var shouldShowRationale by remember { mutableStateOf(false) }
    val result = multiplePermissionsState.permissions.all {
        shouldShowRationale = it.status.shouldShowRationale
        it.status == PermissionStatus.Granted
    }
    if (result) {
        grantedContent()
    } else {
        deniedContent(shouldShowRationale)
    }
}