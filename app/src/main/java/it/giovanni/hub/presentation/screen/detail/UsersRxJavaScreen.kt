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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.User
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.presentation.viewmodel.UsersViewModel
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.cards.MultiSizeCard
import it.giovanni.hub.ui.items.circles.LoadingCircles
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.ui.items.rememberScreenSize
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun UsersRxJavaScreen(
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.users_rxjava),
    topics = listOf(
        "hiltViewModel",
        "RxJava",
        "DataSource",
        "MutableStateFlow",
        "StateFlow",
        "MultiSizeCard"
    )
) { paddingValues ->
    val state: AlertBarState = rememberAlertBarState()
    viewModel.fetchUsersWithRxJava(page = 1, state = state)

    val users: List<User> by viewModel.users.collectAsState()

    AlertBarContent(
        position = AlertBarPosition.TOP,
        alertBarState = state,
        successMaxLines = 3,
        errorMaxLines = 3
    ) {
        ShowRxJavaUsers(users = users, paddingValues = paddingValues)
    }
}

@Composable
fun ShowRxJavaUsers(users: List<User>, paddingValues: PaddingValues) {

    LazyColumn(
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        if (users.isEmpty()) {
            item {
                LoadingCircles()
            }
        }
        items(
            items = users,
            key = { it.id }
        ) { user: User ->
            Spacer(modifier = Modifier.height(height = 4.dp))
            MultiSizeCard(user = user, screenSize = rememberScreenSize())
            Spacer(modifier = Modifier.height(height = 4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersRxScreenPreview() {
    UsersRxJavaScreen(navController = rememberNavController())
}