package it.giovanni.hub.presentation.screen.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.presentation.viewmodel.UIViewModel
import it.giovanni.hub.ui.items.cards.ExpandableCard
import it.giovanni.hub.ui.items.CircularIndicator
import it.giovanni.hub.ui.items.SelectableItem
import it.giovanni.hub.ui.items.rainbowColors

@Composable
fun UIScreen(navController: NavController) {

    val brush = remember { Brush.horizontalGradient(colors = rainbowColors) }

    var selected1 by remember { mutableStateOf(false) }
    var selected2 by remember { mutableStateOf(false) }

    val viewModel: UIViewModel = viewModel()
    val seconds: Any by viewModel.seconds.collectAsState(initial = "00")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brush),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                ExpandableCard(
                    modifier = Modifier.padding(all = 12.dp),
                    title = "Expandable Card"
                )

                var value by remember {
                    mutableIntStateOf(0)
                }
                CircularIndicator(
                    indicatorValue = value
                )
                TextField(
                    value = value.toString(),
                    onValueChange = { input ->
                        value = if (input.isNotEmpty()) {
                            input.toInt()
                        } else {
                            0
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(12.dp))

                SelectableItem(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    selected = selected1,
                    title = "Selectable Item 1",
                    onClick = {
                        selected1 = !selected1
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SelectableItem(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    selected = selected2,
                    title = "Selectable Item 2",
                    subtitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.",
                    onClick = {
                        selected2 = !selected2
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextVerticalAnimation(seconds = seconds, slideOutVertically = true)
                    TextVerticalAnimation(seconds = seconds, slideOutVertically = false)
                    TextHorizontalAnimation(seconds = seconds, slideOutHorizontally = true)
                    TextHorizontalAnimation(seconds = seconds, slideOutHorizontally = false)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TextVerticalAnimation(seconds: Any, slideOutVertically: Boolean) {
    Column(
        // modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = seconds,
            transitionSpec = {
                addVerticalAnimation(
                    duration = 800,
                    slideOutVertically = slideOutVertically
                ).using(SizeTransform(clip = false))
            }, label = "Animated Content"
        ) { targetCount ->
            Text(
                text = "$targetCount",
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TextHorizontalAnimation(seconds: Any, slideOutHorizontally: Boolean) {
    Column(
        // modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = seconds,
            transitionSpec = {
                addHorizontalAnimation(
                    duration = 800,
                    slideOutHorizontally = slideOutHorizontally
                ).using(SizeTransform(clip = false))
            }, label = "Animated Content"
        ) { targetCount ->
            Text(
                text = "$targetCount",
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                textAlign = TextAlign.Center
            )
        }
    }
}

@ExperimentalAnimationApi
fun addVerticalAnimation(duration: Int, slideOutVertically: Boolean): ContentTransform {
    return (slideInVertically(animationSpec = tween(durationMillis = duration)) {
            height -> height } + fadeIn(animationSpec = tween(durationMillis = duration))
            ).togetherWith(slideOutVertically(animationSpec = tween(durationMillis = duration)) {
                height -> if (slideOutVertically) -height else height } +
                fadeOut(animationSpec = tween(durationMillis = duration)))
}

@ExperimentalAnimationApi
fun addHorizontalAnimation(duration: Int, slideOutHorizontally: Boolean): ContentTransform {
    return (slideInHorizontally(animationSpec = tween(durationMillis = duration)) {
            height -> height } +fadeIn(animationSpec = tween(durationMillis = duration))
            ).togetherWith(slideOutHorizontally(animationSpec = tween(durationMillis = duration)) {
                height -> if (slideOutHorizontally) -height else height } +
                fadeOut(animationSpec = tween(durationMillis = duration)))
}

@Preview(showBackground = true)
@Composable
fun UIScreenPreview() {
    UIScreen(navController = rememberNavController())
}