package it.giovanni.hub.presentation.screen.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.navigation.navgraph.MainNavGraph
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.HubBottomAppBar
import it.giovanni.hub.ui.items.HubModalNavigationDrawer
import it.giovanni.hub.utils.Globals.bottomAppBarRoutes
import it.giovanni.hub.utils.Globals.getCurrentRoute1

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
@Composable
fun MainScreen(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    onThemeUpdated: () -> Unit,
    onColorUpdated: () -> Unit,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    counterService: CounterService
) {
    val currentRoute = getCurrentRoute1(navController = navController)

    var currentPage by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = {3})

    // Observe changes in pagerState.currentPage to update currentPage.
    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                currentPage = page
            }
    }

    // Update currentPage.
    /*
    LaunchedEffect(key1 = pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }
    */

    // Update pagerState.
    LaunchedEffect(key1 = currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }

    // Handle back press
    BackHandler(enabled = currentPage != 0) {
        currentPage = 0
    }

    val configuration = LocalConfiguration.current
    val screenWidth = remember(key1 = configuration) {
        mutableIntStateOf(configuration.screenWidthDp)
    }
    val navigationDrawerPadding = screenWidth.intValue / 3

    val pageOffset = currentPage + pagerState.currentPageOffsetFraction

    // Show the drawer only on main routes.
    if (currentRoute in bottomAppBarRoutes) {
        HubModalNavigationDrawer(
            darkTheme = darkTheme,
            dynamicColor = dynamicColor,
            onThemeUpdated = onThemeUpdated,
            onColorUpdated = onColorUpdated,
            mainViewModel = mainViewModel,
            navController = navController,
            currentPage = currentPage,
            onPageSelected = { page ->
                currentPage = page
            }
        ) {
            HorizontalPager(
                state = pagerState,
                // modifier = Modifier.padding(start = if (currentPage == 0) navigationDrawerPadding.dp else 0.dp),
                // userScrollEnabled = true
            ) { index ->
                when (index) {
                    0 -> HomeScreen(navController, mainViewModel)
                    1 -> ProfileScreen(navController)
                    2 -> SettingsScreen(navController)
                }
            }
        }
    } else {
        MainNavGraph(
            navController = navController,
            mainViewModel = mainViewModel,
            counterService = counterService
        )
    }

    /*
    // Simple implementation without HorizontalPager and NavigationDrawer.
    Scaffold(
        bottomBar = {
            HubBottomAppBar(
                navController = navController,
                currentPage = currentPage,
                onPageSelected = {}
            )
        }
    ) { paddingValues ->
        Log.i("", "paddingValues: $paddingValues")
        MainNavGraph(
            navController = navController,
            mainViewModel = mainViewModel,
            counterService = counterService
        )
    }
    */
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        darkTheme = true,
        dynamicColor = false,
        onThemeUpdated = {},
        onColorUpdated = {},
        navController = rememberNavController(),
        mainViewModel = hiltViewModel(),
        counterService = CounterService()
    )
}