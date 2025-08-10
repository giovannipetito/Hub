package it.giovanni.hub.presentation.screen.main

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
import it.giovanni.hub.navigation.routes.TopAppBarsRoutes
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.ui.items.buttons.MainTextButton
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun TopAppBarsScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "TopAppBar",
        "Center Aligned TopAppBar",
        "Medium TopAppBar",
        "Large TopAppBar",
        "Search TopAppBar",
        "Collapsing TopAppBar"
    )

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.top_app_bars),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = TopAppBarsRoutes.HubTopAppBar)
                              },
                    id = R.string.top_app_bar
                )
            }
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = TopAppBarsRoutes.HubCenterAlignedTopAppBar)
                    },
                    id = R.string.center_aligned_top_app_bar
                )
            }
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = TopAppBarsRoutes.HubMediumTopAppBar)
                    },
                    id = R.string.medium_top_app_bar
                )
            }
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = TopAppBarsRoutes.HubLargeTopAppBar)
                    },
                    id = R.string.large_top_app_bar
                )
            }
            item {
                MainTextButton(
                    onClick = {
                        navController.navigate(route = TopAppBarsRoutes.HubCollapsingTopAppBar)
                    },
                    id = R.string.collapsing_top_app_bar
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarsScreenPreview() {
    TopAppBarsScreen(navController = rememberNavController())
}