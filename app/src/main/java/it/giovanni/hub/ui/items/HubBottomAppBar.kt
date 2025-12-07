package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import it.giovanni.hub.navigation.routes.BottomBarRoutes
import it.giovanni.hub.utils.Globals.getCurrentRoute

@Composable
fun HubBottomAppBar(
    navController: NavHostController,
    currentPage: Int,
    onPageSelected: (Int) -> Unit
) {
    val currentRoute: String? = getCurrentRoute(navController)

    if (BottomBarRoutes.entries.any { it.route == currentRoute }) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            actions = {
                BottomBarRoutes.entries.forEachIndexed { index, screen ->

                    val isSelected: Boolean = index == currentPage

                    val iconRes: Int = if (isSelected) {
                        screen.iconOn
                    } else {
                        screen.iconOff
                    }

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
                        val iconPainter = painterResource(id = iconRes)
                        Icon(
                            painter = iconPainter,
                            contentDescription = screen.label,
                            modifier = Modifier.size(size = 28.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // scope.launch(Dispatchers.IO) {}
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary, // BottomAppBarDefaults.bottomAppBarFabColor,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                ) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = addIcon(),
                        contentDescription = "Add Icon",
                        tint = Color.Unspecified
                    )
                }
            }
        )
    }
}