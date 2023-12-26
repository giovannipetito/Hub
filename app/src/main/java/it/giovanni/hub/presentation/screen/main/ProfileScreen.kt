package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.ui.items.buttons.MainTextButton

@Composable
fun ProfileScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 24.dp) // top = it.calculateTopPadding()
    ) {
        item {
            MainTextButton(onClick = {
                // navController.navigate(route = MainSet.Detail1.route) // Per navigare senza passare parametri.
                navController.navigate(route = MainSet.Detail1.passRequiredArguments(6, "Giovanni"))
                                }, id = R.string.detail_1
            )
            MainTextButton(onClick = {
                // navController.navigate(route = MainSet.Detail2.route) // Per navigare senza passare parametri.
                navController.navigate(route = MainSet.Detail2.passOptionalArguments(name = "Giovanni"))
                                }, id = R.string.detail_2
            )
            MainTextButton(onClick = {
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
                                }, id = R.string.detail_3
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.PersonState.route)
                                }, id = R.string.state_and_events
            )
            MainTextButton(onClick = {
                navController.navigate(route = Graph.AUTH_ROUTE)
                                }, id = R.string.auth_sign_up
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Users.route)
                                }, id = R.string.users_coroutines
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.UsersRx.route)
                                }, id = R.string.users_rxjava
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Paging.route)
                                }, id = R.string.paging_3
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.SinglePermission.route)
                                }, id = R.string.single_permission
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.MultiplePermissions.route)
                                }, id = R.string.multiple_permissions
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.Hyperlink.route)
                                }, id = R.string.hyperlink
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.WebView.route)
                                }, id = R.string.web_view
            )
            MainTextButton(onClick = {
                navController.navigate(route = MainSet.CounterService.route)
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