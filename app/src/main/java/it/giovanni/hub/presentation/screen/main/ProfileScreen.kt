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
                // navController.navigate(route = ProfileRoutes.Detail1.route) // Per navigare senza passare parametri.
                navController.navigate(route = ProfileRoutes.Detail1.passRequiredArguments(6, "Giovanni"))
                                }, id = R.string.detail_1
            )
            MainTextButton(onClick = {
                // navController.navigate(route = ProfileRoutes.Detail2.route) // Per navigare senza passare parametri.
                navController.navigate(route = ProfileRoutes.Detail2.passOptionalArguments(name = "Giovanni"))
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
                navController.navigate(route = ProfileRoutes.Detail3.route)
                                }, id = R.string.detail_3
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.PersonState.route)
                                }, id = R.string.state_and_events
            )
            MainTextButton(onClick = {
                navController.navigate(route = Graph.AUTH_ROUTE)
                                }, id = R.string.auth_sign_up
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.Contacts.route)
                                     }, id = R.string.contacts
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.Header.route)
                                     }, id = R.string.header
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.StickyHeader.route)
                                     }, id = R.string.sticky_header
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.SwipeActions.route)
                                     }, id = R.string.swipe_actions
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.UsersCoroutines.route)
                                }, id = R.string.users_coroutines
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.UsersRxJava.route)
                                }, id = R.string.users_rxjava
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.Paging.route)
                                }, id = R.string.paging_3
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.SinglePermission.route)
                                }, id = R.string.single_permission
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.MultiplePermissions.route)
                                }, id = R.string.multiple_permissions
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.Hyperlink.route)
                                }, id = R.string.hyperlink
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.WebView.route)
                                }, id = R.string.web_view
            )
            MainTextButton(onClick = {
                navController.navigate(route = ProfileRoutes.CounterService.route)
                                }, id = R.string.counter_service
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}