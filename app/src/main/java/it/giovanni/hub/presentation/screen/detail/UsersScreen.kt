package it.giovanni.hub.presentation.screen.detail

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.MainActivity
import it.giovanni.hub.data.model.User
import it.giovanni.hub.presentation.viewmodel.UsersViewModel
import it.giovanni.hub.ui.items.Card1

@Composable
fun UsersScreen(
    navController: NavController,
    mainActivity: MainActivity,
    viewModel: UsersViewModel = hiltViewModel()
) {

    viewModel.fetchUsersWithCoroutines(1)

    val users: List<User> by viewModel.users.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        mainActivity.log("[USERS]", "users: $users")
        ShowUsers(users)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowUsers(users: List<User>) {

    val sections = listOf("A", "B", "C", "D", "E", "F", "G")

    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        sections.forEach { section ->
            stickyHeader {
                Text(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(12.dp),
                text = section)
            }

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
                items = users/*.filter { user: User -> user.lastName[0].equals(section) }*/,
                // key = {it.id}
            ) { user: User ->
                Card1(user = user, modifier = Modifier)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersScreenPreview() {
    UsersScreen(navController = rememberNavController(), mainActivity = MainActivity())
}