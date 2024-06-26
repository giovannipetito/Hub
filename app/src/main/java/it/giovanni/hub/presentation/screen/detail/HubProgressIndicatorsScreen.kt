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
import it.giovanni.hub.ui.items.DeterminateCircularIndicator
import it.giovanni.hub.ui.items.IndeterminateCircularIndicator
import it.giovanni.hub.ui.items.IndeterminateLinearIndicator
import it.giovanni.hub.ui.items.DeterminateLinearIndicator
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun HubProgressIndicatorsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.progress_indicators),
    topics = listOf(
        "LinearProgressIndicator",
        "DeterminateLinearIndicator",
        "IndeterminateLinearIndicator",
        "CircularProgressIndicator",
        "DeterminateCircularIndicator",
        "IndeterminateCircularIndicator"
    )
) { paddingValues ->
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        item {
            Spacer(modifier = Modifier.height(height = 24.dp))
            DeterminateLinearIndicator()

            Spacer(modifier = Modifier.height(height = 24.dp))
            IndeterminateLinearIndicator()

            Spacer(modifier = Modifier.height(height = 24.dp))
            DeterminateCircularIndicator()

            Spacer(modifier = Modifier.height(height = 24.dp))
            IndeterminateCircularIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubProgressIndicatorsScreenPreview() {
    HubProgressIndicatorsScreen(navController = rememberNavController())
}