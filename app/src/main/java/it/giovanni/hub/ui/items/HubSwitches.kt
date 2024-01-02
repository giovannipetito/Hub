package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SimpleSwitch(checked: Boolean, onCheckedChange: () -> Unit) {
    Switch(
        checked = checked,
        onCheckedChange = {
            onCheckedChange()
        }
    )
}

@Composable
fun HubSwitch(checked: Boolean, onCheckedChange: () -> Unit) {
    Switch(
        checked = checked,
        onCheckedChange = {
            onCheckedChange()
        },
        thumbContent = if (checked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Icon Switch",
                    modifier = Modifier.size(SwitchDefaults.IconSize
                    )
                )
            }
        } else {
            null
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primaryContainer,
            checkedTrackColor = MaterialTheme.colorScheme.primary,
            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}