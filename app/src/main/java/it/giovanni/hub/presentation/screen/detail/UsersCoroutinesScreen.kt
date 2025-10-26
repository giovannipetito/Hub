package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.domain.usecase.SortBy
import it.giovanni.hub.presentation.viewmodel.UsersCoroutinesViewModel
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.cards.AdaptiveCard
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Globals.getContentPadding
import it.giovanni.hub.utils.Globals.ShimmerItems

@Composable
fun UsersCoroutinesScreen(
    navController: NavController,
    viewModel: UsersCoroutinesViewModel = hiltViewModel()
) {
    var searchResult: String by remember { mutableStateOf("") }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.users_coroutines),
        topics = listOf(
            "hiltViewModel",
            "Coroutines",
            "Repository",
            "UseCase",
            "MutableStateFlow",
            "StateFlow",
            "MultiSizeCard"
        ),
        search = true,
        placeholder = "Search user",
        onTextChangeResult = {
            if (it.isEmpty())
                searchResult = it
        },
        onSearchResult = { result ->
            searchResult = result
        },
        onCloseResult = {
            // searchResult = ""
        }
    ) { paddingValues ->
        val state: AlertBarState = rememberAlertBarState()

        LaunchedEffect(searchResult) {
            val query = searchResult.trim()
            if (query.isEmpty()) {
                viewModel.fetchCoroutinesUsers(page = 2) { result: Result<Unit> ->
                    result
                        .onSuccess { state.addSuccess("Loading successful!") }
                        .onFailure { state.addError(it) }
                }
            } else {
                viewModel.searchCoroutinesUsers(
                    page = 2,
                    query = query,
                    sortBy = SortBy.FIRST_NAME,
                    ascending = true
                ) { result: Result<Unit> ->
                    result.onSuccess { state.addSuccess("Loading successful!") }
                        .onFailure { state.addError(it) } }
            }
        }

        val users: List<User> by viewModel.users.collectAsState()

        AlertBarContent(
            position = AlertBarPosition.BOTTOM,
            alertBarState = state,
            successMaxLines = 3,
            errorMaxLines = 3
        ) {
            ShowCoroutinesUsers(users = users, paddingValues = paddingValues)
        }
    }
}

@Composable
fun ShowCoroutinesUsers(users: List<User>, paddingValues: PaddingValues) {

    LazyColumn(
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
}