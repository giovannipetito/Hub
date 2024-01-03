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
import it.giovanni.hub.ui.items.cards.MultiSizeCard
import it.giovanni.hub.ui.items.rememberScreenSize
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun UsersRxJavaScreen(
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val topics: List<String> = listOf(
        "hiltViewModel",
        "RxJava",
        "DataSource",
        "MutableStateFlow",
        "StateFlow",
        "MultiSizeCard"
    )

    viewModel.fetchUsersWithRxJava(1)

    val users: List<User> by viewModel.users.collectAsState()

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.users_rxjava),
        topics = topics
    ) { paddingValues ->
        ShowUsersRxJava(users = users, paddingValues = paddingValues)
    }
}

@Composable
fun ShowUsersRxJava(users: List<User>, paddingValues: PaddingValues) {

    LazyColumn(
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        if (users.isEmpty()) {
            item {
                HubCircularProgressIndicator()
            }
        }
        items(
            items = users,
            key = { it.id }
        ) { user: User ->
            Spacer(modifier = Modifier.height(4.dp))
            MultiSizeCard(user = user, screenSize = rememberScreenSize())
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersRxScreenPreview() {
    UsersRxJavaScreen(navController = rememberNavController())
}