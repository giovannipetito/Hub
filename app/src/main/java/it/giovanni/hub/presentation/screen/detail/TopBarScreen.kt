package it.giovanni.hub.presentation.screen.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.presentation.viewmodel.TextFieldsViewModel
import it.giovanni.hub.ui.items.AppBarContainer
import it.giovanni.hub.utils.SearchWidgetState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopBarScreen(navController: NavController, viewModel: TextFieldsViewModel = viewModel()) {

    val searchWidgetState: State<SearchWidgetState> = viewModel.searchWidgetState
    val searchTextState: State<String> = viewModel.searchTextState

    Scaffold(
        topBar = {
            AppBarContainer(
                searchWidgetState = searchWidgetState.value,
                searchTextState = searchTextState.value,
                onTextChange = {
                    viewModel.updateSearchTextState(newValue = it)
                },
                onCloseClicked = {
                    viewModel.updateSearchTextState(newValue = "")
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    Log.d("[SEARCH]", it)
                },
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
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
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarScreenPreview() {
    TopBarScreen(navController = rememberNavController())
}