package it.giovanni.hub.ui.items

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.giovanni.hub.utils.Globals.bottomAppBarRoutes
import it.giovanni.hub.utils.Globals.getCurrentRoute1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HubModalNavigationDrawer(
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit,
) {
    val currentRoute = getCurrentRoute1(navController = navController)

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope: CoroutineScope = rememberCoroutineScope()

    // Intercept back button press.
    if (drawerState.isOpen) {
        BackHandler {
            scope.launch {
                drawerState.close()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(250.dp) // Set a fixed width
            ) {
                // Drawer content
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ThemeSwitcher(
                        darkTheme = darkTheme,
                        size = 50.dp,
                        padding = 5.dp,
                        onClick = onThemeUpdated
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Drawer Item") },
                    selected = false,
                    onClick = {
                        // Handle the navigation or action.
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                HorizontalDivider()
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            floatingActionButton = {
                // Show the FAB only on main routes.
                if (currentRoute in bottomAppBarRoutes) {
                    ExtendedFloatingActionButton(
                        // Use navController for navigation if needed.
                        text = { Text("") },
                        icon = { Icon(Icons.Filled.Menu, contentDescription = "") },
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open()
                                    else close()
                                }
                            }
                        }
                    )
                }
            },
            bottomBar = {
                HubBottomAppBar(navController = navController)
            }
        ) { paddingValues ->
            // Screen content
            content(paddingValues) // MainNavGraph will be displayed here.
        }
    }
}