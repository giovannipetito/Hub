package it.giovanni.hub.ui.items

import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import it.giovanni.hub.BuildConfig
import it.giovanni.hub.R
import it.giovanni.hub.data.datasource.local.DataStoreRepository
import it.giovanni.hub.navigation.Login
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.screen.main.ProfileScreen
import it.giovanni.hub.presentation.screen.main.SettingsScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.utils.Globals.mainRoutes
import it.giovanni.hub.utils.Globals.getCurrentRoute
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
    credentialManager: CredentialManager,
    currentPage: Int,
    pagerState: PagerState,
    onPageSelected: (Int) -> Unit,
) {
    val currentRoute = getCurrentRoute(navController = navController)

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope: CoroutineScope = rememberCoroutineScope()
    val context: Context = LocalContext.current
    val repository = DataStoreRepository(context)

    var isLogoutLoading by remember { mutableStateOf(false) }
    var isSignoutLoading by remember { mutableStateOf(false) }

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
        Log.i("[Pager]", "BackHandler - currentPage: $currentPage")
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

    fun kickOut() {
        drawerScope.launch {
            if (mainViewModel.getSignedInUser() != null) {
                mainViewModel.signOut(credentialManager = credentialManager)
                isLogoutLoading = false
                isSignoutLoading = false
            }

            mainViewModel.saveLoginState(state = false)

            navController.popBackStack()
            navController.navigate(route = Login) {
                popUpTo(route = Login)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(width = 250.dp)
            ) {
                Spacer(modifier = Modifier.height(height = 12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(size = 100.dp)
                            .clip(shape = CircleShape),
                        painter = painterResource(id = R.drawable.logo_audioslave),
                        contentDescription = "Circular Image"
                    )
                }

                Spacer(modifier = Modifier.height(height = 12.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(height = 12.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ThemeSwitcher(
                        darkTheme = darkTheme,
                        size = 50.dp,
                        padding = 5.dp,
                        onClick = onThemeUpdated
                    )
                }

                Spacer(modifier = Modifier.height(height = 12.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ColorSwitcher(
                        dynamicColor = dynamicColor,
                        size = 50.dp,
                        padding = 5.dp,
                        onClick = onColorUpdated
                    )
                }

                NavigationDrawerItem(
                    label = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Reset Theme",
                            textAlign = TextAlign.Center
                        )
                    },
                    selected = false,
                    onClick = {
                        drawerScope.launch {
                            repository.resetTheme()
                        }
                    }
                )

                HorizontalDivider()

                NavigationDrawerItem(
                    label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(modifier = Modifier.weight(1f), text = "Logout")
                            if (isLogoutLoading) {
                                HubProgressIndicator(modifier = Modifier.size(size = 32.dp), strokeWidth = 2.dp)
                            }
                        }
                    },
                    selected = false,
                    onClick = {
                        isLogoutLoading = true
                        kickOut()
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(modifier = Modifier.weight(1f), text = "Sign-out")
                            if (isSignoutLoading) {
                                HubProgressIndicator(modifier = Modifier.size(size = 32.dp), strokeWidth = 2.dp)
                            }
                        }
                    },
                    selected = false,
                    onClick = {
                        drawerScope.launch(Dispatchers.IO) {
                            repository.resetEmail()
                        }
                        isSignoutLoading = true
                        kickOut()
                    }
                )

                NavigationDrawerItem(
                    label = { Text(text = "Close") },
                    selected = false,
                    onClick = {
                        // Handle the navigation or action.
                        drawerScope.launch {
                            drawerState.close()
                        }
                    }
                )

                HorizontalDivider()

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(alpha = 0.5f)
                        .padding(vertical = 12.dp),
                    text = "App version: " + BuildConfig.VERSION_NAME,
                    textAlign = TextAlign.Center
                )

                HorizontalDivider()
            }
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
                    Log.i("[Pager]", "index: $index")
                    Log.i("[Pager]", "pagerState.currentPage: ${pagerState.currentPage}")
                    Log.i("[Pager]", "currentPage: $currentPage")
                    when (index) {
                        0 -> HomeScreen(
                            navController = navController,
                            mainViewModel = mainViewModel
                        )
                        1 -> ProfileScreen(navController = navController)
                        2 -> SettingsScreen(navController = navController)
                    }
                }
            }
        }
    }
}