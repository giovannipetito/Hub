package it.giovanni.hub.presentation.screen.detail

import android.Manifest
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.*
import androidx.navigation.NavController
import it.giovanni.hub.domain.usecase.PermissionsManager.RequestMultiplePermissions

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
                text = "Hyperlink Granted!",
                showButton = false
            ) {}
        }
    )
}