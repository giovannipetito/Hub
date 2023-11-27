package it.giovanni.hub.presentation.screen.detail

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.utils.Constants.TOP_BAR_HEIGHT
import it.giovanni.hub.utils.Globals.isScrolled

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CollapsingTopBarScreen(navController: NavController) {

    val lazyListState: LazyListState = rememberLazyListState()

    Scaffold(
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                MainContent(lazyListState = lazyListState)
                TopBar(lazyListState = lazyListState)
            }
        }
    )
}

@Composable
fun TopBar(lazyListState: LazyListState) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .animateContentSize(animationSpec = tween(durationMillis = 300))
            .height(height = if (lazyListState.isScrolled) 0.dp else (TOP_BAR_HEIGHT)),
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentPadding = PaddingValues(start = 16.dp),
    ) {
        Text(
            text = "Collapsing TopBar",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
fun MainContent(lazyListState: LazyListState) {

    val padding = animateDpAsState(
        targetValue = if (lazyListState.isScrolled) 0.dp else (TOP_BAR_HEIGHT),
        animationSpec = tween(durationMillis = 300),
        label = "Padding"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = padding.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        state = lazyListState
    ) {
        item {
            Text(
                text = "Text Spacer 1",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 2",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 3",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 4",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 5",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 6",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Text Spacer 7",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 8",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 9",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 10",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 11",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 12",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 11",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Text Spacer 12",
                color = Color.Blue,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollapsingTopBarScreenPreview() {
    CollapsingTopBarScreen(navController = rememberNavController())
}