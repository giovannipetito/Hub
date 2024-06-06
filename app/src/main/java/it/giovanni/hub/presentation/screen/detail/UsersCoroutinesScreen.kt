package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.User
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.presentation.viewmodel.UsersViewModel
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.cards.AdaptiveCard
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Globals.getContentPadding
import it.giovanni.hub.utils.Globals.ShimmerItems

@Composable
fun UsersCoroutinesScreen(
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.users_coroutines),
    topics = listOf(
        "hiltViewModel",
        "Coroutines",
        "DataSource",
        "MutableStateFlow",
        "StateFlow",
        "MultiSizeCard"
    )
) { paddingValues ->
    val state: AlertBarState = rememberAlertBarState()
    viewModel.fetchUsersWithCoroutines(page = 1, state = state)

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

@Preview(showBackground = true)
@Composable
fun UsersScreenPreview() {
    UsersCoroutinesScreen(navController = rememberNavController())
}