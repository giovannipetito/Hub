package it.giovanni.hub.presentation.screen.main

import android.widget.Toast
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
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import it.giovanni.hub.R
import it.giovanni.hub.data.repositoryimpl.local.DataStoreRepository
import it.giovanni.hub.data.dto.SignInResponse
import it.giovanni.hub.navigation.routes.BottomBarRoutes
import it.giovanni.hub.navigation.routes.LoginRoutes
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.ListDialog
import it.giovanni.hub.ui.items.buttons.LoginButton
import it.giovanni.hub.ui.items.OutlinedTextFieldEmail
import it.giovanni.hub.ui.items.OutlinedTextFieldPassword
import it.giovanni.hub.ui.items.buttons.GoogleButton
import it.giovanni.hub.ui.items.heartIcon
import it.giovanni.hub.ui.items.infoEmptyIcon
import it.giovanni.hub.ui.items.infoIcon
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
    credentialManager: CredentialManager
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
        // email = mutableStateOf(TextFieldValue(""))
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

    val signInResponse: SignInResponse by mainViewModel.signInResponse.collectAsState()

    LaunchedEffect(key1 = signInResponse.user) {
        if (signInResponse.user != null) {
            Toast.makeText(context, "Sign in successful", Toast.LENGTH_SHORT).show()

            mainViewModel.resetState()

            navController.popBackStack()
            navController.navigate(route = BottomBarRoutes.Home.route) {
                popUpTo(route = BottomBarRoutes.Home.route)
            }
        }
    }

    LaunchedEffect(key1 = signInResponse.errorMessage) {
        if (signInResponse.errorMessage != "") {
            signInResponse.errorMessage?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
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
                        modifier = Modifier.size(size = 24.dp),
                        painter = infoEmptyIcon(),
                        contentDescription = "Info Icon",
                        tint = getTransitionColor()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextFieldEmail(modifier = Modifier.weight(weight = 16f), email = email)

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
                    modifier = Modifier
                        .weight(weight = 2f)
                        .scale(scale = scaleIcon.value),
                    enabled = isEmailValid,
                    onClick = {
                        selected = !selected
                        scope.launch(Dispatchers.IO) {
                            repository.saveEmail(email = email.value.text)
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = heartIcon(),
                        contentDescription = "Heart Icon",
                        tint = if (isEmailValid) Color.Red else getTransitionColor()
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextFieldPassword(modifier = Modifier.weight(weight = 16f), password = password)

                IconButton(
                    modifier = Modifier.weight(weight = 2f),
                    onClick = {
                        showDialog.value = true
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = infoIcon(),
                        contentDescription = "Info Icon",
                        tint = getTransitionColor()
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
                        navController.navigate(route = BottomBarRoutes.Home.route) {
                            popUpTo(route = BottomBarRoutes.Home.route)
                        }
                    }
                }
            )

            GoogleButton(
                isLoading = mainViewModel.isLoading.value,
                onClick = {
                    mainViewModel.setLoading(true)
                    scope.launch {
                        mainViewModel.signIn(context = context, credentialManager = credentialManager)
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

            ListDialog(
                title = "The password must:",
                list = listOf(
                    "• contain at least one digit",
                    "• be between 8 and 20 characters long",
                    "• contain at least one lowercase letter",
                    "• contain at least one uppercase letter",
                    "• contain at least one special character"
                ),
                confirmButtonText = "Close",
                showDialog = showDialog,
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