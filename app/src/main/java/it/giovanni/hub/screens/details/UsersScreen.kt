package it.giovanni.hub.screens.details

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
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.User
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.Card1

@Composable
fun UsersScreen(navController: NavController, mainActivity: MainActivity) {

    mainActivity.viewModel.fetchUsers(1)

    val users: List<User> by mainActivity.viewModel.users.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue_200)),
        contentAlignment = Alignment.Center,
    ) {
        ShowUsers(users)
        mainActivity.log("[USERS]", "users: $users")
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

            items(users/*.filter { user: User -> user.lastName[0].equals(section) }*/) { user: User ->
                Card1(user = user)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersScreenPreview() {
    UsersScreen(navController = rememberNavController(), mainActivity = MainActivity())
}