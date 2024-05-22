package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.routes.ProfileRoutes
import it.giovanni.hub.ui.items.buttons.MainTextButton
import it.giovanni.hub.utils.Constants.STATUS_BAR_HEIGHT

@Composable
fun ProfileScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = STATUS_BAR_HEIGHT)
    ) {
        item {
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.Detail1(id = 1, name = "Giovanni"))
                                }, id = R.string.detail_1
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.Detail2(id = 2, name = "Giovanni"))
                                }, id = R.string.detail_2
            )
            MainTextButton(onClick = {
                val person = Person(
                    id = 1,
                    firstName = "Giovanni",
                    lastName = "Petito",
                    visibility = true
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "person",
                    value = person
                )
                navController.navigate(route = ProfileRoutes.Detail3)
                                }, id = R.string.detail_3
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.PersonState)
                                }, id = R.string.state_and_events
            )
            MainTextButton(onClick = {
                navController.navigate(route = Graph.AUTH_ROUTE)
                                }, id = R.string.auth_sign_up
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.Contacts)
                                     }, id = R.string.contacts
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.Header)
                                     }, id = R.string.header
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.StickyHeader)
                                     }, id = R.string.sticky_header
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.SwipeActions)
                                     }, id = R.string.swipe_actions
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.UsersCoroutines)
                                }, id = R.string.users_coroutines
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.UsersRxJava)
                                }, id = R.string.users_rxjava
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.PullToRefresh)
            }, id = R.string.pull_to_refresh
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.Paging)
                                }, id = R.string.paging_3
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.SinglePermission)
                                }, id = R.string.single_permission
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.MultiplePermissions)
                                }, id = R.string.multiple_permissions
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.WebView)
                                }, id = R.string.web_view
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.CounterService)
                                }, id = R.string.counter_service
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.ErrorHandling)
                                     }, id = R.string.error_handling
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}