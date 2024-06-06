package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.PermissionDialog

@Composable
fun PermissionGrantedContent(
    navController: NavController,
    text: String,
    showButton: Boolean = true,
    onClick: () -> Unit
) {
    val topics: List<String> = listOf("PermissionState", "MultiplePermissionsState")

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.permissions),
        topics = topics
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(height = 12.dp))
            if (showButton) {
                Button(onClick = onClick) {
                    Text(text = "Request")
                }
            }
        }
    }
}

@Composable
fun PermissionDeniedContent(
    deniedMessage: String,
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    if (shouldShowRationale) {
        PermissionDialog(
            rationaleMessage = rationaleMessage,
            onRequestPermission = onRequestPermission
        )
    } else {
        PermissionGrantedContent(
            navController = rememberNavController(),
            text = deniedMessage,
            onClick = onRequestPermission
        )
    }
}