package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.buttons.HubButton
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Constants
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun AlertBarScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.alert_bar),
    topics = listOf(
        "rememberUpdatedState",
        "DisposableEffect",
        "AnimatedVisibility",
        "LocalClipboardManager"
    )
) { paddingValues ->
    val state: AlertBarState = rememberAlertBarState()

    var alertBarPosition: AlertBarPosition by remember { mutableStateOf(AlertBarPosition.TOP) }

    var isButtonTopEnabled: Boolean by remember { mutableStateOf(true) }
    var isButtonBottomEnabled: Boolean by remember { mutableStateOf(false) }

    AlertBarContent(
        position = alertBarPosition,
        alertBarState = state,
        successMaxLines = 3,
        errorMaxLines = 3
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            alertBarPosition = AlertBarPosition.TOP
                            isButtonTopEnabled = true
                            isButtonBottomEnabled = false
                        },
                        colors = handleButtonColors(isButtonTopEnabled)
                    ) {
                        Text("Top")
                    }
                    Spacer(modifier = Modifier.width(width = 24.dp))
                    Button(
                        modifier = Modifier.weight(weight = 1f),
                        onClick = {
                            alertBarPosition = AlertBarPosition.BOTTOM
                            isButtonTopEnabled = false
                            isButtonBottomEnabled = true
                        },
                        colors = handleButtonColors(isButtonBottomEnabled)
                    ) {
                        Text(text = "Bottom")
                    }
                }
            }

            item {
                HubButton(
                    modifier = Modifier,
                    text = "Show Success Alert Bar",
                    onClick = {
                        state.addSuccess(success = Constants.LOREM_IPSUM_LONG_TEXT)
                    }
                )

                Spacer(modifier = Modifier.height(height = 12.dp))

                HubButton(
                    modifier = Modifier,
                    text = "Show Error Alert Bar",
                    onClick = {
                        state.addError(exception = Exception(Constants.LOREM_IPSUM_LONG_TEXT))
                    }
                )
            }
        }
    }
}

@Composable
fun handleButtonColors(isButtonEnabled: Boolean): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = if (isButtonEnabled)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.secondaryContainer,
        contentColor = if (isButtonEnabled)
            MaterialTheme.colorScheme.onPrimaryContainer
        else
            MaterialTheme.colorScheme.onSecondaryContainer
    )
}

@Preview(showBackground = true)
@Composable
fun AlertBarScreenPreview() {
    AlertBarScreen(navController = rememberNavController())
}