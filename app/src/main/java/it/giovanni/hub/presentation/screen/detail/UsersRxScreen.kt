package it.giovanni.hub.presentation.screen.detail

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.MainActivity
import it.giovanni.hub.data.model.User
import it.giovanni.hub.ui.items.Card1

@Composable
fun UsersRxScreen(navController: NavController, mainActivity: MainActivity) {

    mainActivity.viewModel.fetchUsersWithRxJava(1)

    val users: List<User> by mainActivity.viewModel.rxUsers.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        mainActivity.log("[USERS]", "users: $users")
        ShowUsersRx(users)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowUsersRx(users: List<User>) {

    var shuffledUsers by remember {
        mutableStateOf(
            users
        )
    }

    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        if (shuffledUsers.isEmpty()) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
        }
        items(
            items = shuffledUsers,
            key = {it.id}
        ) { user: User ->
            Card1(user = user, modifier = Modifier.animateItemPlacement(animationSpec = tween(durationMillis = 600)))
        }
        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                onClick = {
                    shuffledUsers = shuffledUsers.shuffled()
                }
            ) {
                Text("Shuffle")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersRxScreenPreview() {
    UsersRxScreen(navController = rememberNavController(), mainActivity = MainActivity())
}