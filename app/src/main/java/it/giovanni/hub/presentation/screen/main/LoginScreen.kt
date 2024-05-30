package it.giovanni.hub.presentation.screen.main

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import it.giovanni.hub.R
import it.giovanni.hub.data.datasource.local.DataStoreRepository
import it.giovanni.hub.domain.GoogleAuthClient
import it.giovanni.hub.navigation.util.routes.MainRoutes
import it.giovanni.hub.navigation.util.routes.LoginRoutes
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.presentation.viewmodel.SignInViewModel
import it.giovanni.hub.ui.items.InfoDialog
import it.giovanni.hub.ui.items.buttons.LoginButton
import it.giovanni.hub.ui.items.OutlinedTextFieldEmail
import it.giovanni.hub.ui.items.OutlinedTextFieldPassword
import it.giovanni.hub.ui.items.buttons.GoogleButton
import it.giovanni.hub.utils.Globals.checkEmail
import it.giovanni.hub.utils.Globals.checkPassword
import it.giovanni.hub.utils.Globals.getTransitionColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    googleAuthClient: GoogleAuthClient
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

    val showDialog = remember { mutableStateOf(false) }

    /**
     * GOOGLE AUTHENTICATION
     */
    val viewModel: SignInViewModel = viewModel<SignInViewModel>()

    val signInState by viewModel.signInState.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                scope.launch {
                    val signInResponse = googleAuthClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResponse(signInResponse)
                }
            }
        }
    )

    LaunchedEffect(key1 = signInState.isSignInSuccessful) {
        if (signInState.isSignInSuccessful) {
            Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()

            navController.popBackStack()
            navController.navigate(route = MainRoutes.Home.route) {
                popUpTo(route = MainRoutes.Home.route)
            }

            viewModel.resetSignInState()
        }
    }

    LaunchedEffect(key1 = signInState.signInError) {
        signInState.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

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
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = {
                    navController.navigate(route = LoginRoutes.Info)
                }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Info,
                        tint = getTransitionColor(),
                        contentDescription = "Info Icon"
                    )
                }
            }

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
                        tint = if (isEmailValid) Color.Red else getTransitionColor()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextFieldPassword(password = password)

                IconButton(onClick = {
                    showDialog.value = true
                }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Warning,
                        tint = getTransitionColor(),
                        contentDescription = "Warning Icon"
                    )
                }
            }

            validated = (isEmailValid && isPasswordValid)

            LoginButton(
                text = "Log in with email and password",
                loadingText = "Logging in...",
                validated = validated,
                onClick = {
                    if (validated) {

                        mainViewModel.saveLoginState(state = true)

                        navController.popBackStack()
                        navController.navigate(route = MainRoutes.Home.route) {
                            popUpTo(route = MainRoutes.Home.route)
                        }
                    }
                }
            )

            GoogleButton(
                onClick = {
                    scope.launch {
                        val signInIntentSender = googleAuthClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
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

            InfoDialog(
                topics = listOf(
                    "The password must:",
                    "• contain at least one digit",
                    "• be between 8 and 20 characters long",
                    "• contain at least one lowercase letter",
                    "• contain at least one uppercase letter",
                    "• contain at least one special character"
                ),
                showDialog = showDialog,
                onDismissRequest = {
                    showDialog.value = false
                },
                onConfirmation = {
                    showDialog.value = false
                }
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
    // LoginScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}