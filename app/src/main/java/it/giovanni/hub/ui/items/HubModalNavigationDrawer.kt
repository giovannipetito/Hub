package it.giovanni.hub.ui.items

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.screen.main.ProfileScreen
import it.giovanni.hub.presentation.screen.main.SettingsScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.utils.Globals.mainRoutes
import it.giovanni.hub.utils.Globals.getCurrentRoute
import it.giovanni.hub.utils.Globals.getMainBackgroundColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HubModalNavigationDrawer(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    onThemeUpdated: () -> Unit,
    onColorUpdated: () -> Unit,
    mainViewModel: MainViewModel,
    navController: NavHostController,
    credentialManager: CredentialManager,
    currentPage: Int,
    pagerState: PagerState,
    onPageSelected: (Int) -> Unit,
) {
    val currentRoute = getCurrentRoute(navController = navController)

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope: CoroutineScope = rememberCoroutineScope()

    // Observe changes in pagerState.currentPage to update currentPage.
    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                onPageSelected(page)
            }
    }

    // Update pagerState.
    LaunchedEffect(key1 = currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }

    // Handle back press
    BackHandler(enabled = currentPage != 0) {
        onPageSelected(0)
    }

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
            HubModalDrawerSheet(
                drawerState = drawerState,
                drawerScope = drawerScope,
                darkTheme = darkTheme,
                dynamicColor = dynamicColor,
                onThemeUpdated = onThemeUpdated,
                onColorUpdated = onColorUpdated,
                mainViewModel = mainViewModel,
                navController = navController,
                credentialManager = credentialManager
            )
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            floatingActionButton = {
                // Show the FAB only in main routes.
                if (currentRoute in mainRoutes) {
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
                        icon = {
                            var icon: Int
                            drawerState.apply {
                                icon = if (isClosed) R.drawable.ico_enter
                                else R.drawable.ico_exit
                            }
                            Icon(
                                modifier = Modifier.size(size = 24.dp),
                                painter = painterResource(id = icon),
                                contentDescription = "Enter/Exit Icon"
                            )
                        },
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
        ) { paddingValues ->
            Box(modifier = Modifier
                .fillMaxSize()
                .background(brush = getMainBackgroundColors())
                .padding(bottom = paddingValues.calculateBottomPadding()),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPager(
                    state = pagerState
                ) { index ->
                    when (index) {
                        0 -> HomeScreen(navController = navController, mainViewModel = mainViewModel)
                        1 -> ProfileScreen(navController = navController)
                        2 -> SettingsScreen(navController = navController)
                    }
                }
            }
        }
    }
}