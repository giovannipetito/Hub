package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.Graph.AUTH_ROUTE
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.utils.Constants

@Composable
fun HomeScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 56.dp)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            // navController.navigate(route = MainSet.Detail1.route) // Per navigare senza passare parametri.
                            navController.navigate(
                                route = MainSet.Detail1.passRequiredArguments(
                                    6,
                                    "Giovanni"
                                )
                            )
                        },
                    text = "Open Detail 1",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp, // MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            // navController.navigate(route = MainSet.Detail2.route) // Per navigare senza passare parametri.
                            navController.navigate(
                                route = MainSet.Detail2.passOptionalArguments(
                                    name = "Giovanni"
                                )
                            )
                        },
                    text = "Open Detail 2",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            val person = Person(firstName = "Giovanni", lastName = "Petito", visibility = true)
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "person",
                                value = person
                            )
                            navController.navigate(route = MainSet.Detail3.route)
                        },
                    text = "Open Detail 3",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.PersonState.route)
                        },
                    text = "State & Events",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = AUTH_ROUTE)
                        },
                    text = "Auth/Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.TextFields.route)
                        },
                    text = "Text Fields",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Users.route)
                        },
                    text = "Users - Coroutines",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.UsersRx.route)
                        },
                    text = "Users - RxJava",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.UI.route)
                        },
                    text = "UI",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Shimmer.route)
                        },
                    text = "Shimmer",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Shuffled.route)
                        },
                    text = "Shuffled items",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Paging.route)
                        },
                    text = "Paging 3",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            mainViewModel.saveLoginState(state = false)
                            navController.popBackStack()
                            navController.navigate(route = Graph.LOGIN_ROUTE) {
                                popUpTo(Graph.LOGIN_ROUTE)
                            }
                        },
                    text = "Logout",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Hyperlink.route)
                        },
                    text = "Hyperlink",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.SinglePermission.route)
                        },
                    text = "Single Permission",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.MultiplePermissions.route)
                        },
                    text = "Multiple Permissions",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.WebView.route)
                        },
                    text = "WebView",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                SubList()
            }
        }
    }
}

@Composable
fun SubList() {
    val icons = Constants.icons + Constants.icons
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(count = icons.size) {index ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.Blue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icons[index],
                    contentDescription = "Icon",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}