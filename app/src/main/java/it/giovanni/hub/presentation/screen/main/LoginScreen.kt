package it.giovanni.hub.presentation.screen.main

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import it.giovanni.hub.R
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.BottomAppBarSet
import it.giovanni.hub.navigation.util.set.LoginSet
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.buttons.LoginButton
import it.giovanni.hub.ui.items.OutlinedTextFieldEmail
import it.giovanni.hub.ui.items.OutlinedTextFieldPassword
import it.giovanni.hub.utils.Globals.checkEmail
import it.giovanni.hub.utils.Globals.checkPassword
import it.giovanni.hub.utils.Globals.getTransitionColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val composition: LottieComposition? by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.background_universe)
    )
    val progress: Float by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

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

    val isEmailValid = checkEmail(email = email.value.text)
    val isPasswordValid = checkPassword(password = password.value.text)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_universe_static),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        LottieAnimation(
            modifier = Modifier.fillMaxSize(),
            composition = composition,
            progress = { progress },
            contentScale = ContentScale.Crop
        )
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
                    enabled = isEmailValid,
                    onClick = {
                        selected = !selected
                        scope.launch(Dispatchers.IO) {
                            repository.saveEmail(email = email.value.text)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite Icon",
                        tint = if (isEmailValid) Color.Red else Color.Gray
                    )
                }
            }

            OutlinedTextFieldPassword(password = password)

            validated = (isEmailValid && isPasswordValid)

            /*
            LoginButton(
                text = "Log in",
                loadingText = "Logging in",
                validated = validated,
                onClicked = {
                    if (validated) {

                        mainViewModel.saveLoginState(state = true)

                        var route = ""

                        // Se vengo da LoadingScreen (Primo accesso):
                        if (navController.graph.startDestinationRoute == Graph.LOADING_ROUTE) {
                            route = Graph.MAIN_ROUTE // Navigate to MainScreen.
                        } // Se vengo da HomeScreen (Logout/Sign-out):
                        else if (navController.graph.startDestinationRoute == BottomAppBarSet.Home.route) {
                            route = Graph.BOTTOM_ROUTE // Navigate to MainNavGraph.
                        }
                        navController.popBackStack()
                        navController.navigate(route) {
                            popUpTo(route)
                        }
                    }
                }
            )
            */

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                onClick = {
                    if (validated) {
                        mainViewModel.saveLoginState(state = true)

                        var route = ""

                        // Se vengo da LoadingScreen (Primo accesso):
                        if (navController.graph.startDestinationRoute == Graph.LOADING_ROUTE) {
                            route = Graph.MAIN_ROUTE // Navigate to MainScreen.
                        } // Se vengo da HomeScreen (Logout):
                        else if (navController.graph.startDestinationRoute == BottomAppBarSet.Home.route) {
                            route = Graph.BOTTOM_ROUTE // Navigate to MainNavGraph.
                        }
                        navController.popBackStack()
                        navController.navigate(route) {
                            popUpTo(route)
                        }
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(id = R.drawable.ico_audioslave),
                    contentDescription = "Login Button",
                    tint = Color.Unspecified,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Log in",
                    color = if (isSystemInDarkTheme()) {
                        if (validated) Color.Black
                        else Color.Black.copy(alpha = 0.5f)
                    } else {
                        if (validated) Color.White
                        else Color.White.copy(alpha = 0.5f)
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                /*
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                */
            }

            /**
             *
             */

            /**
             *
             */

            Text(
                modifier = Modifier.clickable {
                    navController.navigate(route = LoginSet.Info.route)
                },
                text = "Info",
                color = getTransitionColor(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = showEmail(email.value.text, savedEmail.value).toString(),
                    color = getTransitionColor(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Text(
                    text = password.value.text,
                    color = getTransitionColor(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }
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