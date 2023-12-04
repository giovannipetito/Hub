package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.giovanni.hub.navigation.util.set.BottomAppBarSet

@Composable
fun HubBottomAppBar(navController: NavHostController) {

    val currentRoute: String? = getCurrentRoute(navController)
    var itemColor: Color = MaterialTheme.colorScheme.primary

    if (BottomAppBarSet.entries.any { it.route == currentRoute }) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = itemColor,
            // contentPadding = PaddingValues(start = 24.dp, end = 24.dp),
            actions = {
                BottomAppBarSet.entries.forEach { screen ->

                    val isSelected = screen.route == currentRoute
                    itemColor = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)

                    IconButton(
                        modifier = Modifier.weight(weight = 1f),
                        onClick = {
                            navController.navigate(route = screen.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            screen.icon,
                            contentDescription = screen.contentDescription,
                            tint = itemColor,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* do something */ },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer, // BottomAppBarDefaults.bottomAppBarFabColor,
                    contentColor = MaterialTheme.colorScheme.tertiary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(Icons.Filled.Add, "Localized description")
                }
            }
        )
    }
}

@Composable
fun getCurrentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}