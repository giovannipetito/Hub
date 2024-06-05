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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import it.giovanni.hub.navigation.util.routes.MainRoutes
import it.giovanni.hub.utils.Globals.getCurrentRoute

@Composable
fun HubBottomAppBar(
    navController: NavHostController,
    currentPage: Int,
    onPageSelected: (Int) -> Unit
) {
    val currentRoute: String? = getCurrentRoute(navController)
    var itemColor: Color = MaterialTheme.colorScheme.primary

    if (MainRoutes.entries.any { it.route == currentRoute }) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = itemColor,
            actions = {
                MainRoutes.entries.forEachIndexed { index, screen ->

                    val isSelected = index == currentPage

                    itemColor = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)

                    IconButton(
                        modifier = Modifier.weight(weight = 1f),
                        onClick = {
                            // if (currentPage != index) {
                                onPageSelected(index)
                                navController.navigate(route = screen.route) {
                                    popUpTo(id = navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                }
                            // }
                        }
                    ) {
                        Icon(
                            screen.icon,
                            contentDescription = screen.label,
                            tint = itemColor,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // Do something.
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary, // BottomAppBarDefaults.bottomAppBarFabColor,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(imageVector = Icons.Filled.Add, "Add")
                }
            }
        )
    }
}