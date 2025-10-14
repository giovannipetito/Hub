package it.giovanni.hub.presentation.screen.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import it.giovanni.hub.R
import it.giovanni.hub.utils.Globals.getContentPadding

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(navController: NavController) {

    val url = remember { mutableStateOf("https://www.google.com/") }
    val currentUrl = remember { mutableStateOf(url.value) }
    val isLoading = remember { mutableStateOf(false) }
    val progress = remember { mutableFloatStateOf(0f) }
    val hasError = remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val webViewState = remember { mutableStateOf<WebView?>(null) }

    // Reload URL when Check is pressed
    LaunchedEffect(url.value) {
        webViewState.value?.loadUrl(url.value)
    }

    Scaffold(
        topBar = {
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
                    Row(horizontalArrangement = Arrangement.Start) {
                        IconButton(onClick = {
                            webViewState.value?.goBack()
                        }) {
                            Icon(
                                modifier = Modifier.size(size = 24.dp),
                                painter = painterResource(id = R.drawable.ico_back),
                                contentDescription = "Back Icon"
                            )
                        }
                        IconButton(onClick = {
                            webViewState.value?.goForward()
                        }) {
                            Icon(
                                modifier = Modifier.size(size = 24.dp),
                                painter = painterResource(id = R.drawable.ico_forward),
                                contentDescription = "Forward Icon"
                            )
                        }
                    }
                },
                actions = {
                    Row(horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = {
                            webViewState.value?.reload()
                        }) {
                            Icon(
                                modifier = Modifier.size(size = 24.dp),
                                painter = painterResource(id = R.drawable.ico_refresh),
                                contentDescription = "Refresh Icon"
                            )
                        }
                        IconButton(onClick = {
                            url.value = currentUrl.value
                        }) {
                            Icon(
                                modifier = Modifier.size(size = 24.dp),
                                painter = painterResource(id = R.drawable.ico_done),
                                contentDescription = "Check Icon"
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding()),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = getContentPadding(paddingValues = it)
            ) {
                item {
                    Row(modifier = Modifier
                        .height(height = 48.dp)
                        .background(color = Color.White)
                        .border(width = 2.dp, color = Color.Green)
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .weight(weight = 9f)
                                .fillMaxHeight()
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            value = currentUrl.value,
                            onValueChange = { input -> currentUrl.value = input },
                            textStyle = TextStyle(
                                color = Color.Blue,
                                textAlign = TextAlign.Start,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            ),
                            singleLine = true
                        )
                        if (hasError.value) {
                            Icon(
                                modifier = Modifier.weight(1f),
                                painter = painterResource(id = R.drawable.ico_error),
                                contentDescription = "Error Icon",
                                tint = Color.Red
                            )
                        }
                    }
                }

                // Progress indicator
                item {
                    if (isLoading.value) {
                        LinearProgressIndicator(
                            progress = { progress.floatValue },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // WebView
                item {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { context ->
                            WebView(context).apply {
                                settings.javaScriptEnabled = true
                                webViewClient = object : WebViewClient() {
                                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                        super.onPageStarted(view, url, favicon)
                                        isLoading.value = true
                                        hasError.value = false
                                        Log.d("WebView", "Page started: $url")
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        isLoading.value = false
                                        Log.d("WebView", "Page finished: $url")
                                    }

                                    override fun onReceivedError(
                                        view: WebView?,
                                        request: WebResourceRequest?,
                                        error: WebResourceError?
                                    ) {
                                        super.onReceivedError(view, request, error)
                                        hasError.value = true
                                    }
                                }
                                webChromeClient = object : WebChromeClient() {
                                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                        progress.floatValue = newProgress / 100f
                                    }
                                }
                                loadUrl(url.value)
                                webViewState.value = this
                            }
                        },
                        update = { webView ->
                            if (webView.url != url.value) {
                                webView.loadUrl(url.value)
                            }
                        }
                    )
                }
            }
        }
    )
}