package it.giovanni.hub.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.Data
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.MyCard

@Composable
fun UsersRxScreen(navController: NavController, mainActivity: MainActivity) {

    mainActivity.viewModel.fetchRxUsers(1)

    val users: List<Data> by mainActivity.viewModel.rxUsers.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue_200)),
        contentAlignment = Alignment.Center,
    ) {
        ShowUsersRx(users)
        mainActivity.log("[USERS]", "users: $users")
    }
}

@Composable
fun ShowUsersRx(users: List<Data>) {
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
        items(users) {data: Data ->
            MyCard(data = data)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersRxScreenPreview() {
    UsersScreen(navController = rememberNavController(), mainActivity = MainActivity())
}