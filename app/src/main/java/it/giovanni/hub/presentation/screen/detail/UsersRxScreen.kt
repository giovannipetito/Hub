package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.data.model.User
import it.giovanni.hub.presentation.viewmodel.UsersViewModel
import it.giovanni.hub.ui.items.cards.AdaptiveCard

@Composable
fun UsersRxScreen(
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) {

    viewModel.fetchUsersWithRxJava(1)

    val users: List<User> by viewModel.rxUsers.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        ShowUsersRx(users)
    }
}

@Composable
fun ShowUsersRx(users: List<User>) {

    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        if (users.isEmpty()) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
        }
        items(
            items = users,
            key = {it.id}
        ) { user: User ->
            AdaptiveCard(user = user, modifier = Modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersRxScreenPreview() {
    UsersRxScreen(navController = rememberNavController())
}