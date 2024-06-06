package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import it.giovanni.hub.utils.Constants.CUSTOM_TOP_BAR_HEIGHT
import it.giovanni.hub.utils.SearchWidgetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBarContainer(
    scrollBehavior: TopAppBarScrollBehavior,
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    selected: Boolean,
    onNavigationClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onCloseClicked: () -> Unit
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            TopAppBarDefault(
                scrollBehavior = scrollBehavior,
                selected = selected,
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
    selected: Boolean,
    onNavigationClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onNavigationClicked) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu Icon")
            }
        },
        title = {
            Text(text = "Search")
        },
        colors =
        if (selected) {
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                scrolledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                titleContentColor = MaterialTheme.colorScheme.primary
            )
        } else {
            TopAppBarDefaults.topAppBarColors()
        },
        actions = {
            IconButton(
                onClick = {
                    onSearchClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
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
            leadingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
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
        selected = false,
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