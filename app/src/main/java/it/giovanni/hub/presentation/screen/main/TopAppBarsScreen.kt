package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.navigation.util.set.TopAppBarsSet
import it.giovanni.hub.ui.items.MainText
import it.giovanni.hub.utils.Constants.BOTTOM_BAR_HEIGHT

@Composable
fun TopAppBarsScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = BOTTOM_BAR_HEIGHT)
        ) {
            item {
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = TopAppBarsSet.HubTopAppBar.route)
                        },
                    id = R.string.top_app_bar
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = TopAppBarsSet.HubCenterAlignedTopAppBar.route)
                        },
                    id = R.string.center_aligned_top_app_bar
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = TopAppBarsSet.HubMediumTopAppBar.route)
                        },
                    id = R.string.medium_top_app_bar
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = TopAppBarsSet.HubLargeTopAppBar.route)
                        },
                    id = R.string.large_top_app_bar
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = TopAppBarsSet.HubSearchTopAppBar.route)
                        },
                    id = R.string.search_top_app_bar
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = TopAppBarsSet.HubCollapsingTopAppBar.route)
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