package it.giovanni.hub.presentation.screen.main

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
import it.giovanni.hub.navigation.util.set.TopAppBarsSet
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.ui.items.buttons.MainTextButton

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
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                MainTextButton(onClick = {
                    navController.navigate(route = TopAppBarsSet.HubTopAppBar.route)
                                         }, id = R.string.top_app_bar
                )
                MainTextButton(onClick = {
                    navController.navigate(route = TopAppBarsSet.HubCenterAlignedTopAppBar.route)
                                         }, id = R.string.center_aligned_top_app_bar
                )
                MainTextButton(onClick = {
                    navController.navigate(route = TopAppBarsSet.HubMediumTopAppBar.route)
                                         }, id = R.string.medium_top_app_bar
                )
                MainTextButton(onClick = {
                    navController.navigate(route = TopAppBarsSet.HubLargeTopAppBar.route)
                                         }, id = R.string.large_top_app_bar
                )
                MainTextButton(onClick = {
                    navController.navigate(route = TopAppBarsSet.HubSearchTopAppBar.route)
                                         }, id = R.string.search_top_app_bar
                )
                MainTextButton(onClick = {
                    navController.navigate(route = TopAppBarsSet.HubCollapsingTopAppBar.route)
                                         }, id = R.string.collapsing_top_app_bar
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