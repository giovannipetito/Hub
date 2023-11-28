package it.giovanni.hub

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import it.giovanni.hub.domain.service.StopwatchService
import it.giovanni.hub.navigation.navgraph.RootNavGraph
import it.giovanni.hub.ui.theme.HubTheme
import it.giovanni.hub.presentation.viewmodel.MainViewModel

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var navController: NavHostController

    private val mainViewModel: MainViewModel by viewModels()

    private var isBound by mutableStateOf(false)
    private lateinit var stopwatchService: StopwatchService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as StopwatchService.StopwatchBinder
            stopwatchService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopwatchService::class.java).also { intent ->
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

        // By calling enableEdgeToEdge, I can make my app display edge-to-edge (using the entire
        // width and height of the display) by drawing behind the system bars.
        /*
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.BLACK, Color.WHITE)
        )
        */

        setContent {
            // Use dynamic colors: dynamicColor = true
            // Use customized colors: dynamicColor = false
            HubTheme(dynamicColor = false) {
                navController = rememberNavController()
                if (isBound)
                    RootNavGraph(navController = navController, mainViewModel = mainViewModel, stopwatchService = stopwatchService)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(Manifest.permission.POST_NOTIFICATIONS)
        }
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