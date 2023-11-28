package it.giovanni.hub.presentation.screen.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.domain.service.ServiceHelper
import it.giovanni.hub.domain.service.StopwatchService
import it.giovanni.hub.domain.service.StopwatchState
import it.giovanni.hub.ui.items.addVerticalAnimation
import it.giovanni.hub.utils.Constants.ACTION_SERVICE_CANCEL
import it.giovanni.hub.utils.Constants.ACTION_SERVICE_START
import it.giovanni.hub.utils.Constants.ACTION_SERVICE_STOP

@ExperimentalAnimationApi
@Composable
fun CounterServiceScreen(
    navController: NavController,
    stopwatchService: StopwatchService
) {
    val context = LocalContext.current
    val hours = stopwatchService.hours
    val minutes = stopwatchService.minutes
    val seconds = stopwatchService.seconds
    val currentState = stopwatchService.currentState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(weight = 9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = hours,
                transitionSpec = {
                    addVerticalAnimation(
                        duration = 800,
                        slideOutVertically = true
                    ).using(SizeTransform(clip = false))
                }, label = "Animated Content"
            ) { hours ->
                Text(
                    text = hours.value,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (hours.value == "00") Color.White else Blue
                    )
                )
            }
            AnimatedContent(
                targetState = minutes,
                transitionSpec = {
                    addVerticalAnimation(
                        duration = 800,
                        slideOutVertically = true
                    ).using(SizeTransform(clip = false))
                }, label = "Animated Content"
            ) { minutes ->
                Text(
                    text = minutes.value,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (minutes.value == "00") Color.White else Blue
                    )
                )
            }
            AnimatedContent(
                targetState = seconds,
                transitionSpec = {
                    addVerticalAnimation(
                        duration = 800,
                        slideOutVertically = true
                    ).using(SizeTransform(clip = false))
                }, label = "Animated Content"
            ) { seconds ->
                Text(
                    text = seconds.value,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (seconds.value == "00") Color.White else Blue
                    )
                )
            }
        }
        Row(modifier = Modifier.weight(weight = 1f)) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.8f),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = if (currentState.value == StopwatchState.Started) ACTION_SERVICE_STOP
                        else ACTION_SERVICE_START
                    )
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentState.value == StopwatchState.Started) Red else Blue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (currentState.value == StopwatchState.Started) "Stop"
                    else if ((currentState.value == StopwatchState.Stopped)) "Resume"
                    else "Start"
                )
            }
            Spacer(modifier = Modifier.width(30.dp))
            Button(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.8f),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context, action = ACTION_SERVICE_CANCEL
                    )
                },
                enabled = seconds.value != "00" && currentState.value != StopwatchState.Started,
                colors = ButtonDefaults.buttonColors(disabledContainerColor = Gray)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun CounterServiceScreenPreview() {
    CounterServiceScreen(navController = rememberNavController(), stopwatchService = StopwatchService())
}