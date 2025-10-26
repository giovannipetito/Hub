package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.presentation.viewmodel.UsersCoroutinesViewModel
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.cards.AdaptiveCard
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Globals.getContentPadding
import it.giovanni.hub.utils.Globals.ShimmerItems
import kotlinx.coroutines.launch

@Composable
fun PullToRefreshScreen(
    navController: NavController,
    viewModel: UsersCoroutinesViewModel = hiltViewModel()
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.pull_to_refresh),
    topics = listOf(
        "rememberPullToRefreshState"
    )
) { paddingValues ->
    val scope = rememberCoroutineScope()

    val state: AlertBarState = rememberAlertBarState()

    LaunchedEffect(Unit) {
        viewModel.fetchCoroutinesUsers(page = 2) { result: Result<Unit> ->
            result.onSuccess { state.addSuccess("Loading successful!") }
                .onFailure { state.addError(it) }
        }
    }

    val users: List<User> by viewModel.users.collectAsState()

    AlertBarContent(
        position = AlertBarPosition.BOTTOM,
        alertBarState = state,
        successMaxLines = 3,
        errorMaxLines = 3
    ) {
        ShowPullToRefreshUsers(
            users = users,
            paddingValues = paddingValues,
            isRefreshing = viewModel.isRefreshing.collectAsState().value,
            onRefresh = {
                scope.launch {
                    // viewModel.isRefreshing.value = true
                    viewModel.fetchCoroutinesUsers(page = 2) { result: Result<Unit> ->
                        result.onSuccess { state.addSuccess("Loading successful!") }
                            .onFailure { state.addError(it) }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPullToRefreshUsers(
    users: List<User>,
    paddingValues: PaddingValues,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    // val lazyListState: LazyListState = rememberLazyListState()
    val pullToRefreshState: PullToRefreshState = rememberPullToRefreshState()
    Box(
        modifier = Modifier
            // .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        LazyColumn(
            // state = lazyListState,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            if (users.isEmpty()) {
                item {
                    ShimmerItems()
                }
            }

            items(
                items = users,
                key = { it.id }
            ) { user: User ->
                Spacer(modifier = Modifier.height(height = 4.dp))
                AdaptiveCard(user = user, modifier = Modifier)
                Spacer(modifier = Modifier.height(height = 4.dp))
            }
        }

        /*
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                onRefresh()
            }
        }

        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }
        
        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            containerColor = if (pullToRefreshState.verticalOffset == 0.0f)
                Color.Transparent else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (pullToRefreshState.verticalOffset == 0.0f)
                Color.Transparent else MaterialTheme.colorScheme.onPrimaryContainer
        )
        */
    }
}

@Preview(showBackground = true)
@Composable
fun PullToRefreshScreenPreview() {
    PullToRefreshScreen(navController = rememberNavController())
}