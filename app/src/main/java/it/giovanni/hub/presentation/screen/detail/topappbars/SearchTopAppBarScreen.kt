package it.giovanni.hub.presentation.screen.detail.topappbars

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.presentation.viewmodel.TextFieldsViewModel
import it.giovanni.hub.ui.items.SearchTopAppBarContainer
import it.giovanni.hub.ui.items.LazyColumn1
import it.giovanni.hub.utils.SearchWidgetState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchTopAppBarScreen(
    navController: NavController,
    viewModel: TextFieldsViewModel = viewModel()
) {
    val searchWidgetState: State<SearchWidgetState> = viewModel.searchWidgetState
    val searchTextState: State<String> = viewModel.searchTextState

    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var selected by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchTopAppBarContainer(
                scrollBehavior = scrollBehavior,
                searchWidgetState = searchWidgetState.value,
                searchTextState = searchTextState.value,
                onTextChange = {
                    viewModel.updateSearchTextState(newValue = it)
                },
                selected = selected,
                onNavigationClicked = {
                    selected = !selected
                },
                onSearchClicked = {
                    Log.d("[SEARCH]", it)
                },
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                },
                onCloseClicked = {
                    viewModel.updateSearchTextState(newValue = "")
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /*
                    scope.launch {
                        snackBarHostState.showSnackbar("SnackBar")
                    }
                    */

                    scope.launch {
                        val result = snackBarHostState
                            .showSnackbar(
                                message = "SnackBar",
                                actionLabel = "Action",
                                // Defaults to SnackbarDuration.Short
                                duration = SnackbarDuration.Indefinite
                            )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                /* Handle snackbar action performed */
                            }
                            SnackbarResult.Dismissed -> {
                                /* Handle snackbar dismissed */
                            }
                        }
                    }
                },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                contentColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "FAB")
            }
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn1(it)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SearchTopAppBarScreenPreview() {
    SearchTopAppBarScreen(navController = rememberNavController())
}