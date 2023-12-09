package it.giovanni.hub.presentation.screen.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.web.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(navController: NavController) {

    val url = remember { mutableStateOf("https://www.google.com/") }
    val state = rememberWebViewState(url = url.value)
    val navigator = rememberWebViewNavigator()
    val textFieldValue = remember(state.content.getCurrentUrl()) {
        mutableStateOf(state.content.getCurrentUrl() ?: "")
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Column {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    "WebView",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = {
                        navigator.navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                    IconButton(onClick = {
                        navigator.navigateForward()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "ArrowForward"
                        )
                    }
                }
            },
            actions = {
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        navigator.reload()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                    IconButton(onClick = {
                        url.value = textFieldValue.value
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Check"
                        )
                    }
                }
            },
            scrollBehavior = scrollBehavior,
        )

        Row(modifier = Modifier
            .height(48.dp)
            .background(color = Color.White)
            .border(width = 2.dp, color = Color.Green)
        ) {
            BasicTextField(
                modifier = Modifier
                    .weight(9f)
                    .fillMaxHeight()
                    .padding(8.dp, 8.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                value = textFieldValue.value,
                onValueChange = { input -> textFieldValue.value = input },
                textStyle = TextStyle(
                    color = Color.Blue,
                    textAlign = TextAlign.Start,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ),
                maxLines = 1
            )
            if (state.errorsForCurrentRequest.isNotEmpty()) {
                Icon(
                    modifier = Modifier.weight(1f),
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color.Red
                )
            }
        }

        val loadingState = state.loadingState
        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                progress = { loadingState.progress },
                modifier = Modifier.fillMaxWidth()
            )
        }

        val webClient = remember {
            object : AccompanistWebViewClient() {
                override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Log.d("Accompanist WebView", "Page started loading for $url")
                }
            }
        }

        WebView(
            state = state,
            modifier = Modifier.weight(1f),
            navigator = navigator,
            onCreated = { webView ->
                webView.settings.javaScriptEnabled = true
            },
            client = webClient
        )
    }
}