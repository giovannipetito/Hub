package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.TextFieldsViewModel
import it.giovanni.hub.ui.items.InfoDialog
import it.giovanni.hub.ui.items.HubSearchTopAppBar
import it.giovanni.hub.utils.SearchWidgetState

// State: si definisce State qualsiasi valore che può cambiare nel tempo.
// Event: notifica a una parte di un programma che è successo qualcosa.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    navController: NavController,
    title: String = stringResource(id = R.string.app_name),
    topics: List<String> = emptyList(),
    search: Boolean = false,
    placeholder: String = "Search here...",
    onTextChangeResult: (String) -> Unit = {},
    onSearchResult: (String) -> Unit = {},
    onCloseResult: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val viewModel: TextFieldsViewModel = viewModel()

    val searchWidgetState: State<SearchWidgetState> = viewModel.searchWidgetState
    val searchTextState: State<String> = viewModel.searchTextState

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val showDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HubSearchTopAppBar(
                scrollBehavior = scrollBehavior,
                title = title,
                search = search,
                placeholder = placeholder,
                onInfoClick = {
                    showDialog.value = true
                },
                searchWidgetState = searchWidgetState.value,
                searchTextState = searchTextState.value,
                onTextChange = {
                    onTextChangeResult(it)
                    viewModel.updateSearchTextState(newValue = it)
                },
                onNavigationClicked = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED)
                        navController.popBackStack()
                },
                onSearchClicked = { result ->
                    onSearchResult(result)
                },
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                },
                onCloseClicked = {
                    onCloseResult()
                    viewModel.updateSearchTextState(newValue = "")
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(top = paddingValues.calculateTopPadding()),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.badge_bottom),
                        contentDescription = "Badge Bottom"
                    )
                }
                content(paddingValues)

                InfoDialog(
                    topics = topics,
                    showDialog = showDialog,
                    onConfirmation = {
                        showDialog.value = false
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BaseScreenPreview() {
    BaseScreen(
        navController = rememberNavController(),
        onSearchResult = {},
        content = {}
    )
}