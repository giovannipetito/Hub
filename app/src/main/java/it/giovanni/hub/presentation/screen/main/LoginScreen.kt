package it.giovanni.hub.presentation.screen.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.BottomBarSet
import it.giovanni.hub.navigation.util.set.LoginSet
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.buttons.GoogleButton
import it.giovanni.hub.ui.items.OutlinedTextFieldEmail
import it.giovanni.hub.ui.items.OutlinedTextFieldPassword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = DataStoreRepository(context)
    val savedEmail: State<String> = repository.getEmail().collectAsState(initial = "")

    // Use MutableState to represent TextField state.
    lateinit var email: MutableState<TextFieldValue>
    if (savedEmail.value.isEmpty()) {
        email = remember {
            mutableStateOf(TextFieldValue(savedEmail.value))
        }
    } else {
        email = mutableStateOf(TextFieldValue(""))
        email = remember {
            mutableStateOf(TextFieldValue(savedEmail.value))
        }
    }

    val password: MutableState<TextFieldValue> = remember {
        mutableStateOf(TextFieldValue(""))
    }

    var selected by remember { mutableStateOf(false) }

    var validated by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextFieldEmail(email = email)

                // Favorite Icon animation scope - Start
                val scaleIcon = remember { Animatable(initialValue = 1f) }

                LaunchedEffect(key1 = selected) {
                    if (selected) {
                        val jobIcon: Job = launch {
                            scaleIcon.animateTo(
                                targetValue = 0.3f,
                                animationSpec = tween(
                                    durationMillis = 50
                                )
                            )
                            scaleIcon.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                        jobIcon.join()
                    }
                }
                // Favorite Icon animation scope - End

                IconButton(
                    modifier = Modifier.scale(scale = scaleIcon.value),
                    onClick = {
                        if (email.value.text.isNotEmpty()) { // todo: replace with Email Regex.
                            selected = !selected
                            scope.launch(Dispatchers.IO) {
                                repository.saveEmail(email = email.value.text)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite Icon",
                        tint = if (email.value.text.isNotEmpty()) Color.Red else Color.Gray
                    )
                }
            }

            OutlinedTextFieldPassword(password = password)

            validated = (email.value.text.isNotEmpty() && password.value.text.isNotEmpty())

            GoogleButton(
                text = "Sign Up with Google",
                loadingText = "Creating Account",
                validated = validated,
                onClicked = {
                    if (validated) {

                        mainViewModel.saveLoginState(state = true)

                        var route = ""

                        // Se vengo da LoadingScreen (Primo accesso):
                        if (navController.graph.startDestinationRoute == Graph.LOADING_ROUTE) {
                            route = Graph.MAIN_ROUTE // Navigate to MainScreen.
                        } // Se vengo da HomeScreen (Logout):
                        else if (navController.graph.startDestinationRoute == BottomBarSet.Home.route) {
                            route = Graph.BOTTOM_ROUTE // Navigate to MainNavGraph.
                        }
                        navController.popBackStack()
                        navController.navigate(route) {
                            popUpTo(route)
                        }
                    }
                }
            )

            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = LoginSet.Info.route)
                },
                text = "Info",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Outlined TextField Email: " + showEmail(email.value.text, savedEmail.value),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Outlined TextField Password: " + password.value.text,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun showEmail(currentEmail: String, savedEmail: String?): String? {
    val message: String? = if (savedEmail != "" && currentEmail == "") {
        savedEmail
    } else {
        currentEmail
    }
    return message
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}