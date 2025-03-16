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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import it.giovanni.hub.data.datasource.local.DataStoreRepository
import it.giovanni.hub.navigation.util.routes.MainRoutes
import it.giovanni.hub.utils.Globals.getCurrentRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HubBottomAppBar(
    navController: NavHostController,
    currentPage: Int,
    onPageSelected: (Int) -> Unit
) {
    val currentRoute: String? = getCurrentRoute(navController)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = DataStoreRepository(context)

    val isCustomBottomAppBar = repository.isCustomBottomAppBar().collectAsState(initial = false)

    if (MainRoutes.entries.any { it.route == currentRoute }) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            actions = {
                MainRoutes.entries.forEachIndexed { index, screen ->

                    val isSelected: Boolean = index == currentPage

                    val iconRes: Int = if (isSelected) {
                        screen.iconResOn
                    } else {
                        screen.iconResOff
                    }

                    val itemColor: Color = if (isSelected)
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
                        if (isCustomBottomAppBar.value) {
                            val iconPainter = painterResource(id = iconRes)
                            Icon(
                                painter = iconPainter,
                                contentDescription = screen.label,
                                modifier = Modifier.size(size = 28.dp),
                                tint = Color.Unspecified
                            )
                        } else {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.label,
                                modifier = Modifier.size(size = 36.dp),
                                tint = itemColor
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            repository.setCustomBottomAppBar(!isCustomBottomAppBar.value)
                        }
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