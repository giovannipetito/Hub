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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.ui.items.MainText
import it.giovanni.hub.utils.Constants

@Composable
fun ProfileScreen(navController: NavController) {
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
                    id = R.string.detail_1
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
                    id = R.string.detail_2
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
                    id = R.string.detail_3
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.PersonState.route)
                        },
                    id = R.string.state_and_events
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = Graph.AUTH_ROUTE)
                        },
                    id = R.string.auth_sign_up
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Users.route)
                        },
                    id = R.string.users_coroutines
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.UsersRx.route)
                        },
                    id = R.string.users_rxjava
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Paging.route)
                        },
                    id = R.string.paging_3
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.SinglePermission.route)
                        },
                    id = R.string.single_permission
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.MultiplePermissions.route)
                        },
                    id = R.string.multiple_permissions
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.Hyperlink.route)
                        },
                    id = R.string.hyperlink
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.WebView.route)
                        },
                    id = R.string.web_view
                )
                MainText(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = MainSet.CounterService.route)
                        },
                    id = R.string.counter_service
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}