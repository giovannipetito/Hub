@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun PaneScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "rememberListDetailPaneScaffoldNavigator",
        "NavigableListDetailPaneScaffold",
        "ListDetailPaneScaffoldRole",
        "listPane",
        "detailPane",
        "extraPane",
        "AnimatedPane",
        "AssistChip"
    )

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.pane),
        topics = topics
    ) { paddingValues ->
        AdaptiveLayout(paddingValues = paddingValues)
    }
}

@Composable
fun AdaptiveLayout(paddingValues: PaddingValues) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = getContentPadding(paddingValues = paddingValues)
            ) {
                items(20) {
                    Text(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .clickable {
                                navigator.navigateTo(
                                    pane = ListDetailPaneScaffoldRole.Detail,
                                    content = "Item ${it + 1}"
                                )
                            }
                            .padding(16.dp),
                        text = "Item ${it + 1}"
                    )
                }
            }
        },
        detailPane = {
            val content = navigator.currentDestination?.content?.toString() + " Detail"
            AnimatedPane {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = getContentPadding(paddingValues = paddingValues)
                ) {
                    item {
                        Text(text = content)
                    }
                    item {
                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AssistChip(
                                onClick = {
                                    navigator.navigateTo(
                                        pane = ListDetailPaneScaffoldRole.Extra,
                                        content = "Extra 1"
                                    )
                                },
                                label = {
                                    Text(text = "Extra 1")
                                }
                            )
                            AssistChip(
                                onClick = {
                                    navigator.navigateTo(
                                        pane = ListDetailPaneScaffoldRole.Extra,
                                        content = "Extra 2"
                                    )
                                },
                                label = {
                                    Text(text = "Extra 2")
                                }
                            )
                        }
                    }
                }
            }
        },
        extraPane = {
            val content = navigator.currentDestination?.content?.toString() ?: "Select an extra"
            AnimatedPane {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = content)
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PaneScreenPreview() {
    PaneScreen(navController = rememberNavController())
}