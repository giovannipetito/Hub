package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.buttons.HubElevatedButton
import it.giovanni.hub.ui.items.buttons.HubFilledButton
import it.giovanni.hub.ui.items.buttons.HubFilledTonalButton
import it.giovanni.hub.ui.items.buttons.HubOutlinedButton
import it.giovanni.hub.ui.items.buttons.HubTextButton
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun HubButtonsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.buttons),
    topics = listOf(
        "Button",
        "FilledTonalButton",
        "OutlinedButton",
        "ElevatedButton",
        "TextButton"
    )
) { paddingValues ->
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        item {
            HubFilledButton(onClick = {})
        }

        item {
            HubFilledTonalButton(onClick = {})
        }

        item {
            HubOutlinedButton(onClick = {})
        }

        item {
            HubElevatedButton(onClick = {})
        }

        item {
            HubTextButton(onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubButtonsScreenPreview() {
    HubButtonsScreen(navController = rememberNavController())
}