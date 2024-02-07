package it.giovanni.hub

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.AndroidEntryPoint
import it.giovanni.hub.domain.service.CounterService
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

    @OptIn(ExperimentalFoundationApi::class)
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

        /*

        /**
         * START - GENERATIVE AI
         */

        // Generate text from text-only input

        // Access your API key as a Build Configuration variable
        val apiKey = BuildConfig.apiKey
        val geminiProModel = "gemini-pro" // Model for text-only input.
        val geminiProVisionModel = "gemini-pro-vision" // Model for text-and-images input (multimodal).

        val generativeModel1 = GenerativeModel(
            // modelName indicates the model that's applicable for your use case.
            modelName = geminiProModel,
            apiKey = apiKey
        )

        val prompt = "Write a story about a magic backpack."

        val response1 = generativeModel1.generateContent(prompt)
        print(response1.text)

        // Use streaming with text-only input
        generativeModel1.generateContentStream(prompt).collect { chunk ->
            print(chunk.text)
        }

        // Generate text from text-and-image input (multimodal)

        val generativeModel2 = GenerativeModel(
            modelName = geminiProVisionModel,
            apiKey = apiKey
        )

        val image1: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.logo_audioslave)
        val image2: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.logo_audioslave)

        val inputContent = content {
            image(image1)
            image(image2)
            text("What's different between these pictures?")
        }

        val response2 = generativeModel2.generateContent(inputContent)
        print(response2.text)

        // Use streaming for faster interactions

        var response3 = ""
        generativeModel2.generateContentStream(inputContent).collect { chunk ->
            print(chunk.text)
            response3 += chunk.text
        }

        // multi-turn conversations (chat)

        val generativeModel3 = GenerativeModel(
            modelName = geminiProModel,
            apiKey = apiKey
        )

        val chat1 = generativeModel3.startChat(
            history = listOf(
                content(role = "user") { text("Hello, I have 2 dogs in my house.") },
                content(role = "model") { text("Great to meet you. What would you like to know?") }
            )
        )

        chat1.sendMessage("How many paws are in my house?")

        // Use streaming with multi-turn conversations (like chat)
        val chat2 = generativeModel3.startChat()
        chat2.sendMessageStream(inputContent).collect { chunk ->
            print(chunk.text)
        }

        /**
         * END - GENERATIVE AI
         */

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
                if (isBound) {
                    // Show the drawer only on main routes.
                    if (currentRoute in mainRoutes) {
                        HubModalNavigationDrawer(
                            darkTheme = darkTheme,
                            dynamicColor = hubColor,
                            onThemeUpdated = { darkTheme = !darkTheme },
                            onColorUpdated = { hubColor = !hubColor },
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
                        RootNavGraph(
                            navController = navController,
                            mainViewModel = mainViewModel,
                            counterService = counterService
                        )
                    }
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

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}