package it.giovanni.hub.ui.items

import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import it.giovanni.hub.R

@Composable
fun HubAssistChip() {
    val context = LocalContext.current
    AssistChip(
        onClick = {
            Toast.makeText(context, "AssistChip", Toast.LENGTH_SHORT).show()
                  },
        label = { Text("Assist chip") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ico_audioslave),
                contentDescription = "Settings Icon",
                Modifier.size(size = AssistChipDefaults.IconSize)
            )
        }
    )
}

@Composable
fun HubFilterChip() {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = { selected = !selected },
        label = {
            Text("Filter chip")
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    painter = painterResource(id = R.drawable.ico_audioslave),
                    contentDescription = "Done icon",
                    modifier = Modifier.size(size = FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}

@Composable
fun HubInputChip(
    text: String,
    onDismiss: () -> Unit,
) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(
        onClick = {
            onDismiss()
            enabled = !enabled
        },
        label = { Text(text) },
        selected = enabled,
        avatar = {
            Icon(
                painter = painterResource(id = R.drawable.ico_user),
                contentDescription = "User Icon",
                Modifier.size(size = InputChipDefaults.AvatarSize)
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ico_audioslave),
                contentDescription = "Close Icon",
                Modifier.size(size = InputChipDefaults.AvatarSize)
            )
        }
    )
}

@Composable
fun HubSuggestionChip() {
    val context = LocalContext.current
    SuggestionChip(
        onClick = {
            Toast.makeText(context, "SuggestionChip", Toast.LENGTH_SHORT).show()
                  },
        label = { Text("Suggestion chip") }
    )
}