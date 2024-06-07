package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import it.giovanni.hub.R
import it.giovanni.hub.utils.Constants.CUSTOM_TOP_BAR_HEIGHT
import it.giovanni.hub.utils.SearchWidgetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBarContainer(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String = stringResource(id = R.string.app_name),
    showSearch: Boolean = false,
    onInfoClick: () -> Unit,
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onNavigationClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onCloseClicked: () -> Unit
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            TopAppBarDefault(
                scrollBehavior = scrollBehavior,
                title = title,
                showSearch = showSearch,
                onInfoClick = onInfoClick,
                onNavigationClicked = onNavigationClicked,
                onSearchClicked = onSearchTriggered
            )
        }
        SearchWidgetState.OPENED -> {
            SearchTextField(
                text = searchTextState,
                onTextChange = onTextChange,
                onSearchClicked = onSearchClicked,
                onCloseClicked = onCloseClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDefault(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    showSearch: Boolean,
    onInfoClick: () -> Unit,
    onNavigationClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = Modifier
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ArrowBack Icon"
                    )
                }
            }
        },
        actions = {
            if (showSearch) {
                IconButton(onClick = { onSearchClicked() }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon"
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onInfoClick) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info Icon"
                    )
                }
            }
        }
    )
}

@Composable
fun SearchTextField(
    text: String,
    onTextChange: (String) -> Unit,
    onSearchClicked: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = CUSTOM_TOP_BAR_HEIGHT)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxSize()
                .height(height = CUSTOM_TOP_BAR_HEIGHT),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(text = "Search here...")
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
                                imageVector = Icons.Default.Search,
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
                            imageVector = Icons.Default.Close,
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
            colors = TextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                disabledTextColor = MaterialTheme.colorScheme.tertiary,
                errorTextColor = MaterialTheme.colorScheme.error,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                cursorColor = Color.White,
                errorCursorColor = Color.Red,
                textSelectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                disabledIndicatorColor = MaterialTheme.colorScheme.tertiary,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                disabledLeadingIconColor = MaterialTheme.colorScheme.tertiary,
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
                focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                disabledTrailingIconColor = MaterialTheme.colorScheme.tertiary,
                errorTrailingIconColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                disabledLabelColor = MaterialTheme.colorScheme.tertiary,
                errorLabelColor = MaterialTheme.colorScheme.error,
                focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                disabledPlaceholderColor = MaterialTheme.colorScheme.tertiary,
                errorPlaceholderColor = MaterialTheme.colorScheme.error,
                focusedSupportingTextColor = MaterialTheme.colorScheme.primary,
                unfocusedSupportingTextColor = MaterialTheme.colorScheme.secondary,
                disabledSupportingTextColor = MaterialTheme.colorScheme.tertiary,
                errorSupportingTextColor = MaterialTheme.colorScheme.error,
                focusedPrefixColor = MaterialTheme.colorScheme.primary,
                unfocusedPrefixColor = MaterialTheme.colorScheme.secondary,
                disabledPrefixColor = MaterialTheme.colorScheme.tertiary,
                errorPrefixColor = MaterialTheme.colorScheme.error,
                focusedSuffixColor = MaterialTheme.colorScheme.primary,
                unfocusedSuffixColor = MaterialTheme.colorScheme.secondary,
                disabledSuffixColor = MaterialTheme.colorScheme.secondary,
                errorSuffixColor = MaterialTheme.colorScheme.error
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultAppBarPreview() {
    TopAppBarDefault(
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        title = stringResource(id = R.string.app_name),
        showSearch = false,
        onInfoClick = {},
        onNavigationClicked = {},
        onSearchClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SearchAppBarPreview() {
    SearchTextField(
        text = "Random text",
        onTextChange = {},
        onSearchClicked = {},
        onCloseClicked = {}
    )
}