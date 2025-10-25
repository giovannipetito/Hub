package it.giovanni.hub

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import it.giovanni.hub.data.repositoryimpl.local.DataStoreRepository
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.navigation.navgraph.RootNavGraph
import it.giovanni.hub.ui.theme.HubTheme
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.HubModalNavigationDrawer
import it.giovanni.hub.utils.Globals.getCurrentRoute
import it.giovanni.hub.utils.Globals.mainRoutes
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var credentialManager: CredentialManager

    private var isBound by mutableStateOf(false)
    private lateinit var counterService: CounterService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as CounterService.CounterBinder
            counterService = binder.getService()
            mainViewModel.setCounterService(counterService)
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mainViewModel.firstAccess.value) {
            setTheme(R.style.Theme_Hub)
        } else {
            mainViewModel.setFirstAccess(firstAccess = true)
            mainViewModel.setSplashOpened(state = true)
            installSplashScreen().setKeepOnScreenCondition {
                mainViewModel.keepSplashOpened.value
            }
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

        FirebaseApp.initializeApp(this) // Initialize Firebase Realtime Database.

        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val repository = DataStoreRepository(context)

            val isDarkTheme = isSystemInDarkTheme()

            val darkThemeFlow = remember(isDarkTheme) { repository.isDarkTheme(isDarkTheme = isDarkTheme) }
            val hubColorFlow = remember { repository.isDynamicColor(default = true) }

            val darkTheme by darkThemeFlow.collectAsState(initial = isDarkTheme)
            val hubColor  by hubColorFlow.collectAsState(initial = true)

            var currentPage: Int by remember { mutableIntStateOf(0) }
            val pagerState: PagerState = rememberPagerState(pageCount = {3})

            HubTheme(darkTheme = darkTheme, dynamicColor = !hubColor) {

                val navController: NavHostController = rememberNavController()
                val currentRoute = getCurrentRoute(navController = navController)
                credentialManager = CredentialManager.create(LocalContext.current)

                if (currentRoute !in mainRoutes) {
                    RootNavGraph(
                        navController = navController,
                        mainViewModel = mainViewModel,
                        credentialManager = credentialManager
                    )
                } else {
                    HubModalNavigationDrawer(
                        darkTheme = darkTheme,
                        dynamicColor = hubColor,
                        onThemeUpdated = {
                            val newValue = !darkTheme
                            scope.launch { repository.setDarkTheme(theme = newValue) }
                        },
                        onColorUpdated = {
                            val newValue = !hubColor
                            scope.launch { repository.setDynamicColor(color = newValue) }
                        },
                        mainViewModel = mainViewModel,
                        navController = navController,
                        credentialManager = credentialManager,
                        currentPage = currentPage,
                        pagerState = pagerState,
                        onPageSelected = { page ->
                            currentPage = page
                        }
                    )
                }
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, CounterService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }

    override fun hubLog(tag: String, message: String) {
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