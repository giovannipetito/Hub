package it.giovanni.hub.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.shouldShowRationale

object PermissionsManager {

    @ExperimentalPermissionsApi
    @Composable
    fun RequestSinglePermission(
        permissionState: PermissionState,
        deniedContent: @Composable (Boolean) -> Unit,
        grantedContent: @Composable () -> Unit
    ) {
        when (permissionState.status) {
            is PermissionStatus.Granted -> {
                grantedContent()
            }
            is PermissionStatus.Denied -> {
                deniedContent(permissionState.status.shouldShowRationale)
            }
        }
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
}