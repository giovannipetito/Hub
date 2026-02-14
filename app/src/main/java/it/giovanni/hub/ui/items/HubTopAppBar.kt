package it.giovanni.hub.ui.items

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.utils.Constants.HUB_TOP_BAR_LANDSCAPE_HEIGHT
import it.giovanni.hub.utils.Constants.HUB_TOP_BAR_PORTRAIT_HEIGHT
import it.giovanni.hub.utils.Globals.getTextFieldColors
import it.giovanni.hub.utils.SearchWidgetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    search: Boolean,
    backup: Boolean,
    placeholder: String,
    onInfoClick: () -> Unit,
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onNavigationClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onBackupClicked: () -> Unit,
    onCloseClicked: () -> Unit
) {
    val topAppBarHeight =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
            HUB_TOP_BAR_PORTRAIT_HEIGHT
        else
            HUB_TOP_BAR_LANDSCAPE_HEIGHT

    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            ActionTopAppBar(
                topAppBarHeight = topAppBarHeight,
                scrollBehavior = scrollBehavior,
                title = title,
                search = search,
                backup = backup,
                onInfoClick = onInfoClick,
                onNavigationClicked = onNavigationClicked,
                onSearchClicked = onSearchTriggered,
                onBackupClicked = onBackupClicked
            )
        }
        SearchWidgetState.OPENED -> {
            SearchTopAppBar(
                topAppBarHeight = topAppBarHeight,
                text = searchTextState,
                placeholder = placeholder,
                onTextChange = onTextChange,
                onSearchClicked = onSearchClicked,
                onCloseClicked = onCloseClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionTopAppBar(
    topAppBarHeight: Dp,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    search: Boolean,
    backup: Boolean,
    onInfoClick: () -> Unit,
    onNavigationClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onBackupClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = Modifier
            .height(height = topAppBarHeight)
            .paint(
                painter = painterResource(id = R.drawable.badge_top_large),
                alignment = Alignment.BottomEnd
            ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onNavigationClicked) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = backIcon(),
                        contentDescription = "Back Icon"
                    )
                }
            }
        },
        actions = {
            if (search) {
                IconButton(onClick = { onSearchClicked() }) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = searchIcon(),
                        contentDescription = "Search Icon"
                    )
                }
            }
            if (backup) {
                IconButton(onClick = { onBackupClicked() }) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = cloudBackupIcon(),
                        contentDescription = "Backup Icon"
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onInfoClick) {
                    Icon(
                        modifier = Modifier.size(size = 24.dp),
                        painter = infoEmptyIcon(),
                        contentDescription = "Info Icon"
                    )
                }
            }
        }
    )
}

@Composable
fun SearchTopAppBar(
    topAppBarHeight: Dp,
    text: String,
    placeholder: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = topAppBarHeight)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxSize()
                .height(height = topAppBarHeight)
                .focusRequester(focusRequester = focusRequester),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(text = placeholder)
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            ),
            singleLine = true,
            trailingIcon = {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    if (text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onSearchClicked(text)
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(size = 24.dp),
                                painter = searchIcon(),
                                contentDescription = "Search Icon"
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            if (text.isNotEmpty())
                                onTextChange("")
                            else
                                onCloseClicked()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(size = 24.dp),
                            painter = if (text.isNotEmpty()) deleteIcon() else closeIcon(),
                            contentDescription = "Close Icon"
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = getTextFieldColors()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ActionTopAppBarPreview() {
    ActionTopAppBar(
        topAppBarHeight = 96.dp,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = stringResource(id = R.string.app_name),
        search = false,
        backup = false,
        onInfoClick = {},
        onNavigationClicked = {},
        onSearchClicked = {},
        onBackupClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SearchTopAppBarPreview() {
    SearchTopAppBar(
        topAppBarHeight = 96.dp,
        text = "Search",
        placeholder = "Search here...",
        onTextChange = {},
        onSearchClicked = {},
        onCloseClicked = {}
    )
}