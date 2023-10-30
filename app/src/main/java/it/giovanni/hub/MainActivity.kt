package it.giovanni.hub

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import it.giovanni.hub.navigation.navgraph.RootNavGraph
import it.giovanni.hub.ui.theme.HubTheme
import it.giovanni.hub.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var navController: NavHostController

    val viewModel: MainViewModel by viewModels()

    var keepSplashOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }

        setContent {
            HubTheme {
                val context: Context = LocalContext.current
                val mainActivity: MainActivity = context as MainActivity
                navController = rememberNavController()
                RootNavGraph(navController = navController, mainActivity = mainActivity)
            }
        }
    }

    override fun log(tag: String, message: String) {
        Log.d(tag, message)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun CustomGreeting(text: String) {
    Text(
        text = text,
        style = TextStyle(
            background = MaterialTheme.colorScheme.primary,
            color = MaterialTheme.colorScheme.secondary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HubTheme {
        // Greeting("Android")
        // CustomGreeting(text = "Giovanni")
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    HubTheme {
        // Column1()
        // Column2()
        // Column3()
        // Row1()
        // Box1()
        // Box2()
        // Box3()
        /*
        Column(modifier = Modifier.fillMaxSize()) {
            // Text1()
            // Text2()
            // Text3(MaterialTheme.colorScheme.primary)
            // Text4()
            // Text5()
            ScriptText(
                normalText = "Hello",
                scriptText = "World",
            )
            ScriptText(
                normalText = "Hello",
                scriptText = "World",
                scriptTextFontWeight = FontWeight.Light,
                scriptTextBaselineShift = BaselineShift.Subscript
            )
        }
        */
    }
}