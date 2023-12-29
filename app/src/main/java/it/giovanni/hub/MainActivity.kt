package it.giovanni.hub

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.navigation.navgraph.RootNavGraph
import it.giovanni.hub.ui.theme.HubTheme
import it.giovanni.hub.presentation.viewmodel.MainViewModel

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var navController: NavHostController

    private val mainViewModel: MainViewModel by viewModels()

    private var isBound by mutableStateOf(false)
    private lateinit var counterService: CounterService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as CounterService.CounterBinder
            counterService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, CounterService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
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

            HubTheme(darkTheme = darkTheme, dynamicColor = !hubColor) {
                navController = rememberNavController()
                if (isBound)
                    RootNavGraph(
                        darkTheme = darkTheme,
                        dynamicColor = hubColor,
                        onThemeUpdated = { darkTheme = !darkTheme },
                        onColorUpdated = { hubColor = !hubColor },
                        navController = navController,
                        mainViewModel = mainViewModel,
                        counterService = counterService
                    )
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

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}