package it.giovanni.hub.ui.items

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun rememberAlertBarState(): AlertBarState {
    return remember { AlertBarState() }
}

@Composable
fun AlertBarContent(
    alertBarState: AlertBarState,
    position: AlertBarPosition = AlertBarPosition.TOP,
    errorMaxLines: Int = 1,
    successMaxLines: Int = 1,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()

        var showAlertBar by remember { mutableStateOf(false) }
        val error by rememberUpdatedState(newValue = alertBarState.error?.message)
        val success by rememberUpdatedState(newValue = alertBarState.success)

        DisposableEffect(key1 = alertBarState.updated) {
            showAlertBar = true
            val timer = Timer("Animation Timer", true)
            timer.schedule(3000L) {
                showAlertBar = false
            }
            onDispose {
                timer.cancel()
                timer.purge()
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = if (position == AlertBarPosition.TOP)
                Arrangement.Top else Arrangement.Bottom
        ) {
            AnimatedVisibility(
                visible = alertBarState.error != null && showAlertBar
                        || alertBarState.success != null && showAlertBar,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 300),
                    expandFrom = if (position == AlertBarPosition.TOP)
                        Alignment.Top else Alignment.Bottom
                ),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 300),
                    shrinkTowards = if (position == AlertBarPosition.TOP)
                        Alignment.Top else Alignment.Bottom
                ),
            ) {
                AlertBar(
                    success = success,
                    error = error,
                    errorMaxLines = errorMaxLines,
                    successMaxLines = successMaxLines
                )
            }
        }
    }
}

@Composable
internal fun AlertBar(
    success: String?,
    error: String?,
    errorMaxLines: Int,
    successMaxLines: Int
) {
    val clipboardManager = LocalClipboardManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (error != null) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.primaryContainer
            )
            .padding(vertical = 12.dp)
            .padding(horizontal = 12.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(4f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector =
                if (error != null) Icons.Default.Warning
                else Icons.Default.Check,
                contentDescription = "Alert Bar Icon",
                tint = if (error != null) MaterialTheme.colorScheme.onErrorContainer
                else MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = success ?: (error ?: "Unknown"),
                color =
                if (error != null) MaterialTheme.colorScheme.onErrorContainer
                else MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (error != null) errorMaxLines else successMaxLines
            )
        }
        if (error != null) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(text = "$error"))
                    },
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(
                        text = "Copy",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = MaterialTheme.typography.labelMedium.fontSize,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
@Preview
internal fun AlertBarSuccessPreview() {
    AlertBar(
        success = "Successfully Updated.",
        error = null,
        successMaxLines = 1,
        errorMaxLines = 1
    )
}

@Composable
@Preview
internal fun AlertBarErrorPreview() {
    AlertBar(
        success = null,
        error = "Internet Unavailable.",
        errorMaxLines = 1,
        successMaxLines = 1
    )
}