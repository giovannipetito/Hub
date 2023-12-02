package it.giovanni.hub.ui.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import it.giovanni.hub.navigation.util.set.BottomAppBarSet

@Composable
fun HubBottomAppBar(navController: NavHostController) {
    BottomAppBar(
        actions = {
            IconButton(onClick = {
                navController.navigate(route = BottomAppBarSet.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }) {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            }
            IconButton(onClick = {
                navController.navigate(route = BottomAppBarSet.Profile.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }) {
                Icon(Icons.Filled.Person, contentDescription = "Profile")
            }
            IconButton(onClick = {
                navController.navigate(route = BottomAppBarSet.Settings.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }
    )
}