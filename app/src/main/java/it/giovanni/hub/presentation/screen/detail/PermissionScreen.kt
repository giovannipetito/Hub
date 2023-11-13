package it.giovanni.hub.presentation.screen.detail

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import it.giovanni.hub.domain.usecase.PermissionsManager.RequestSinglePermission

@ExperimentalPermissionsApi
@Composable
fun PermissionScreen(navController: NavController) {

    val permission = Manifest.permission.READ_CONTACTS
    val permissionState = rememberPermissionState(permission)

    val deniedMessage = "Give this app the permission to proceed. If it doesn't work, then you'll have to do it manually from the settings."
    val rationaleMessage = "To use this app's functionalities, you need to give us the permission."

    RequestSinglePermission(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                deniedMessage = deniedMessage,
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = {
                    permissionState.launchPermissionRequest()
                }
            )
        },
        grantedContent = {
            PermissionGrantedContent(
                text = "Permission Granted!",
                showButton = false,
            ) {}
        }
    )
}