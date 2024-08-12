@file:OptIn(ExperimentalAnimationApi::class)

package it.giovanni.hub.presentation.screen.detail

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.service.ServiceHelper
import it.giovanni.hub.domain.service.CounterService
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.addVerticalAnimation
import it.giovanni.hub.utils.Constants.ACTION_SERVICE_CANCEL
import it.giovanni.hub.utils.Constants.ACTION_SERVICE_START
import it.giovanni.hub.utils.Constants.ACTION_SERVICE_STOP
import it.giovanni.hub.utils.CounterState
import it.giovanni.hub.utils.Globals.getContentPadding

@ExperimentalAnimationApi
@Composable
fun CounterServiceScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.counter_service),
    topics = listOf("AnimatedContent", "ServiceHelper", "CounterService")
) {
    val counterService: CounterService = mainViewModel.counterService.value
    val context: Context = LocalContext.current
    val hours = counterService.hours
    val minutes = counterService.minutes
    val seconds = counterService.seconds
    val currentState = counterService.currentState

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = it)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
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
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                            fontWeight = FontWeight.Bold,
                            color =
                            if (hours.value == "00") MaterialTheme.colorScheme.inversePrimary
                            else MaterialTheme.colorScheme.primary
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
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                            fontWeight = FontWeight.Bold,
                            color =
                            if (minutes.value == "00") MaterialTheme.colorScheme.inversePrimary
                            else MaterialTheme.colorScheme.primary
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
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                            fontWeight = FontWeight.Bold,
                            color =
                            if (seconds.value == "00") MaterialTheme.colorScheme.inversePrimary
                            else MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context,
                            action = if (currentState.value == CounterState.Started) ACTION_SERVICE_STOP
                            else ACTION_SERVICE_START
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                        if (currentState.value == CounterState.Started)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.primaryContainer,
                        contentColor =
                        if (currentState.value == CounterState.Started)
                            MaterialTheme.colorScheme.onTertiaryContainer
                        else
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(
                        text = if (currentState.value == CounterState.Started) "Stop"
                        else if ((currentState.value == CounterState.Stopped)) "Resume"
                        else "Start"
                    )
                }

                Spacer(modifier = Modifier.width(width = 24.dp))

                Button(
                    modifier = Modifier.weight(weight = 1f),
                    onClick = {
                        ServiceHelper.triggerForegroundService(
                            context = context, action = ACTION_SERVICE_CANCEL
                        )
                    },
                    enabled = seconds.value != "00" && currentState.value != CounterState.Started,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun CounterServiceScreenPreview() {
    CounterServiceScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}