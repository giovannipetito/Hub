package it.giovanni.hub.presentation.screen.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.HubAssistChip
import it.giovanni.hub.ui.items.HubFilterChip
import it.giovanni.hub.ui.items.HubInputChip
import it.giovanni.hub.ui.items.HubSuggestionChip
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun HubChipsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.chips),
    topics = listOf("AssistChip", "FilterChip", "InputChip", "SuggestionChip")
) { paddingValues ->
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))

            HubAssistChip()

            Spacer(modifier = Modifier.height(12.dp))

            HubFilterChip()

            Spacer(modifier = Modifier.height(12.dp))

            HubInputChip(
                text = "InputChip",
                onDismiss = {
                    Toast.makeText(context, "InputChip", Toast.LENGTH_SHORT).show()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            HubSuggestionChip()

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubChipsScreenPreview() {
    HubChipsScreen(navController = rememberNavController())
}