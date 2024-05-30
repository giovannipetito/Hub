package it.giovanni.hub

import android.Manifest
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import it.giovanni.hub.domain.GoogleAuthClient
import it.giovanni.hub.navigation.navgraph.RootNavGraph
import it.giovanni.hub.presentation.screen.main.HomeScreen
import it.giovanni.hub.presentation.screen.main.ProfileScreen
import it.giovanni.hub.presentation.screen.main.SettingsScreen
import it.giovanni.hub.ui.theme.HubTheme
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.HubModalNavigationDrawer
import it.giovanni.hub.utils.Globals.getCurrentRoute
import it.giovanni.hub.utils.Globals.mainRoutes

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    val googleAuthClient: GoogleAuthClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            signInClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!mainViewModel.firstAccess.value) {
            mainViewModel.setFirstAccess(firstAccess = true)
            mainViewModel.setSplashOpened(state = true)
            installSplashScreen().setKeepOnScreenCondition {
                mainViewModel.keepSplashOpened.value
            }
        } else {
            setTheme(R.style.Theme_Hub)
        }

        // The enableEdgeToEdge method makes the app screen edge-to-edge (using the
        // entire width and height of the display) by drawing behind the system bars.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) // (Color.BLACK, Color.WHITE)
        )

        /*
        // This set the navigationBar completely transparent (it can be used with enableEdgeToEdge).
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        */

        setContent {
            val isDarkTheme: Boolean = isSystemInDarkTheme()
            var darkTheme: Boolean by remember { mutableStateOf(isDarkTheme) }
            var hubColor: Boolean by remember { mutableStateOf(true) }

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

            HubTheme(darkTheme = darkTheme, dynamicColor = !hubColor) {
                val navController: NavHostController = rememberNavController()
                val currentRoute = getCurrentRoute(navController = navController)
                // Show the drawer only on main routes.
                if (currentRoute in mainRoutes) {
                    HubModalNavigationDrawer(
                        darkTheme = darkTheme,
                        dynamicColor = hubColor,
                        onThemeUpdated = { darkTheme = !darkTheme },
                        onColorUpdated = { hubColor = !hubColor },
                        mainViewModel = mainViewModel,
                        navController = navController,
                        googleAuthClient = googleAuthClient,
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
                                0 -> HomeScreen(
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    googleAuthClient = googleAuthClient
                                )
                                1 -> ProfileScreen(navController = navController)
                                2 -> SettingsScreen(navController = navController)
                            }
                        }
                    }
                } else {
                    RootNavGraph(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        googleAuthClient = googleAuthClient
                    )
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // The onConfigurationChanged method handles any specific changes when the configuration changes,
    // such as screen orientation. However, since you're using Jetpack Compose, you might not need to
    // implement any specific code in this method, as Compose handles configuration changes internally.
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle any specific changes here.
    }

    override fun log(tag: String, message: String) {
        Log.d(tag, message)
    }

    private fun requestPermissions(vararg permissions: String) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach {
                Log.d("MainActivity", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }
}