package it.giovanni.hub.ui.items

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.giovanni.hub.R
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.utils.Globals.bottomAppBarRoutes
import it.giovanni.hub.utils.Globals.getCurrentRoute1
import it.giovanni.hub.utils.Globals.getMainBackgroundColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HubModalNavigationDrawer(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    onThemeUpdated: () -> Unit,
    onColorUpdated: () -> Unit,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    currentPage: Int,
    onPageSelected: (Int) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val currentRoute = getCurrentRoute1(navController = navController)

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current

    // Intercept back button press.
    if (drawerState.isOpen) {
        BackHandler {
            drawerScope.launch {
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
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        painter = painterResource(id = R.drawable.logo_audioslave),
                        contentDescription = "Circular Image"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ColorSwitcher(
                        dynamicColor = dynamicColor,
                        size = 50.dp,
                        padding = 5.dp,
                        onClick = onColorUpdated
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text(text = "Logout") },
                    selected = false,
                    onClick = {
                        mainViewModel.saveLoginState(state = false)

                        navController.popBackStack()
                        navController.navigate(route = Graph.LOGIN_ROUTE) {
                            popUpTo(Graph.LOGIN_ROUTE)
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text(text = "Sign-out") },
                    selected = false,
                    onClick = {
                        mainViewModel.saveLoginState(state = false)

                        val repository = DataStoreRepository(context)
                        drawerScope.launch(Dispatchers.IO) {
                            repository.resetEmail()
                        }
                        // todo: Cancellare la foto in LoginScreen

                        navController.popBackStack()
                        navController.navigate(route = Graph.LOGIN_ROUTE) {
                            popUpTo(Graph.LOGIN_ROUTE)
                        }
                    }
                )

                NavigationDrawerItem(
                    label = { Text(text = "Close Drawer") },
                    selected = false,
                    onClick = {
                        // Handle the navigation or action.
                        drawerScope.launch {
                            drawerState.close()
                        }
                    }
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
                        text = {
                            var text: String
                            drawerState.apply {
                                text = if (isClosed) "Open"
                                else "Close"
                            }
                            Text(text = text)
                        },
                        icon = { Icon(Icons.Filled.Menu, contentDescription = "") },
                        onClick = {
                            drawerScope.launch {
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
                HubBottomAppBar(
                    navController = navController,
                    currentPage = currentPage,
                    onPageSelected = onPageSelected
                )
            }
        ) {
            // Screen content
            Box(modifier = Modifier
                .fillMaxSize()
                .background(brush = getMainBackgroundColors())
                .padding(bottom = it.calculateBottomPadding()),
                // .statusBarsPadding().navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                content(it) // MainNavGraph will be displayed here.
            }
        }
    }
}