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
import it.giovanni.hub.ui.items.buttons.HubElevatedButton
import it.giovanni.hub.ui.items.buttons.HubFilledButton
import it.giovanni.hub.ui.items.buttons.HubFilledTonalButton
import it.giovanni.hub.ui.items.buttons.HubOutlinedButton
import it.giovanni.hub.ui.items.buttons.HubTextButton
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
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))

                HubFilledButton(onClick = {})

                Spacer(modifier = Modifier.height(12.dp))

                HubFilledTonalButton(onClick = {})

                Spacer(modifier = Modifier.height(12.dp))

                HubOutlinedButton(onClick = {})

                Spacer(modifier = Modifier.height(12.dp))

                HubElevatedButton(onClick = {})

                Spacer(modifier = Modifier.height(12.dp))

                HubTextButton(onClick = {})
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubButtonsScreenPreview() {
    HubButtonsScreen(navController = rememberNavController())
}