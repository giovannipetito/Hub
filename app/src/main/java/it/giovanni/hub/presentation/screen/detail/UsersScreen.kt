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
import it.giovanni.hub.presentation.viewmodel.UsersViewModel
import it.giovanni.hub.ui.items.HubCircularProgressIndicator
import it.giovanni.hub.ui.items.cards.AdaptiveCard

@Composable
fun UsersScreen(
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val topics: List<String> = listOf(
        "hiltViewModel",
        "Coroutines",
        "DataSource",
        "MutableStateFlow",
        "StateFlow",
        "MultiSizeCard"
    )

    viewModel.fetchUsersWithCoroutines(1)

    val users: List<User> by viewModel.users.collectAsState()

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.users_coroutines),
        topics = topics
    ) { paddingValues ->
        ShowUsers(users = users, paddingValues = paddingValues)
    }
}

@Composable
fun ShowUsers(users: List<User>, paddingValues: PaddingValues) {

    LazyColumn(
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = paddingValues.calculateBottomPadding())
    ) {
        if (users.isEmpty()) {
            item {
                HubCircularProgressIndicator()
            }
        }

        items(
            items = users,
            key = {it.id}
        ) { user: User ->
            Spacer(modifier = Modifier.height(4.dp))
            AdaptiveCard(user = user, modifier = Modifier)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersScreenPreview() {
    UsersScreen(navController = rememberNavController())
}