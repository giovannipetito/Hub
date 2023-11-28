package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.MainText
import it.giovanni.hub.utils.Constants

@Composable
fun ProfileScreen(
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
            contentPadding = PaddingValues(bottom = Constants.BOTTOM_BAR_HEIGHT)
        ) {
            item {
                MainText(
                    modifier = Modifier
                        .clickable {
                            // navController.navigate(route = MainSet.Detail1.route) // Per navigare senza passare parametri.
                            navController.navigate(
                                route = MainSet.Detail1.passRequiredArguments(
                                    6,
                                    "Giovanni"
                                )
                            )
                        },
                    text = "Open Detail 1"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            // navController.navigate(route = MainSet.Detail2.route) // Per navigare senza passare parametri.
                            navController.navigate(
                                route = MainSet.Detail2.passOptionalArguments(
                                    name = "Giovanni"
                                )
                            )
                        },
                    text = "Open Detail 2"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            val person = Person(
                                firstName = "Giovanni",
                                lastName = "Petito",
                                visibility = true
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                key = "person",
                                value = person
                            )
                            navController.navigate(route = MainSet.Detail3.route)
                        },
                    text = "Open Detail 3"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.PersonState.route)
                        },
                    text = "State & Events"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = Graph.AUTH_ROUTE)
                        },
                    text = "Auth/Sign Up"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Users.route)
                        },
                    text = "Users - Coroutines"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.UsersRx.route)
                        },
                    text = "Users - RxJava"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Paging.route)
                        },
                    text = "Paging 3"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.SinglePermission.route)
                        },
                    text = "Single Permission"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.MultiplePermissions.route)
                        },
                    text = "Multiple Permissions"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Hyperlink.route)
                        },
                    text = "Hyperlink"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.WebView.route)
                        },
                    text = "WebView"
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.CounterService.route)
                        },
                    text = "Counter Service"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}