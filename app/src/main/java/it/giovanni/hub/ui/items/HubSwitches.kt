package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import it.giovanni.hub.R

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
                    painter = painterResource(id = R.drawable.ico_done),
                    contentDescription = "Check Icon",
                    modifier = Modifier.size(size = SwitchDefaults.IconSize)
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