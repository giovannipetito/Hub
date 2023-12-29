package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.buttons.ElevatedHubButton
import it.giovanni.hub.ui.items.buttons.FilledHubButton
import it.giovanni.hub.ui.items.buttons.FilledTonalHubButton
import it.giovanni.hub.ui.items.buttons.OutlinedHubButton
import it.giovanni.hub.ui.items.buttons.TextHubButton
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun HubButtonsScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "Button",
        "FilledTonalButton",
        "OutlinedButton",
        "ElevatedButton",
        "TextButton"
    )

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.buttons),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))

                FilledHubButton(onClick = {})

                Spacer(modifier = Modifier.height(12.dp))

                FilledTonalHubButton(onClick = {})

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedHubButton(onClick = {})

                Spacer(modifier = Modifier.height(12.dp))

                ElevatedHubButton(onClick = {})

                Spacer(modifier = Modifier.height(12.dp))

                TextHubButton(onClick = {})
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubButtonsScreenPreview() {
    HubButtonsScreen(navController = rememberNavController())
}