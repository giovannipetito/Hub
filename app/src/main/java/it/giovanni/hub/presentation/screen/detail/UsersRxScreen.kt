package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import it.giovanni.hub.ui.items.cards.MultiSizeCard
import it.giovanni.hub.ui.items.rememberScreenSize

@Composable
fun UsersRxScreen(
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
    ) {
        Box(contentAlignment = Alignment.Center) {
            ShowUsersRx(users)
        }
    }
}

@Composable
fun ShowUsersRx(users: List<User>) {

    LazyColumn(contentPadding = PaddingValues(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)) {
        if (users.isEmpty()) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.Center)
                        .fillMaxSize()
                )
            }
        }
        items(
            items = users,
            key = {it.id}
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
    UsersRxScreen(navController = rememberNavController())
}