package it.giovanni.hub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.navigation.navgraph.SetupNavGraph
import it.giovanni.hub.ui.items.Box3
import it.giovanni.hub.ui.items.Column1
import it.giovanni.hub.ui.items.ScriptText
import it.giovanni.hub.ui.theme.HubTheme
import it.giovanni.hub.ui.theme.Purple40
import it.giovanni.hub.ui.theme.Purple80

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HubTheme {

                navController = rememberNavController()
                SetupNavGraph(navController = navController)

                // A surface container using the 'background' color from the theme
                /*
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
                */
            }
        }
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
            background = Purple80,
            color = Purple40
        )
    )
}

// @Preview(showBackground = true)
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