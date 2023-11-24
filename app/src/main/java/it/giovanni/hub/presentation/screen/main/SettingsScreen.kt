package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.navigation.Graph
import it.giovanni.hub.navigation.util.set.MainSet
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.utils.Constants

@Composable
fun SettingsScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = Constants.BOTTOM_BAR_HEIGHT)
        ) {
            item {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Texts.route)
                        },
                    text = "Texts",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.TextFields.route)
                        },
                    text = "TextFields",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Boxes.route)
                        },
                    text = "Boxes",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Columns.route)
                        },
                    text = "Columns",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Rows.route)
                        },
                    text = "Rows",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.UI.route)
                        },
                    text = "UI",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Reply.route)
                        },
                    text = "Reply",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Shimmer.route)
                        },
                    text = "Shimmer Items",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.Shuffled.route)
                        },
                    text = "Shuffled Items",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.TopBar.route)
                        },
                    text = "TopBar",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(route = MainSet.CollapsingTopBar.route)
                        },
                    text = "Collapsing TopBar",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            mainViewModel.saveLoginState(state = false)
                            navController.popBackStack() // Rimuove SettingsScreen dal back stack.
                            navController.popBackStack() // Rimuove HomeScreen dal back stack.
                            navController.navigate(route = Graph.LOGIN_ROUTE) {
                                popUpTo(Graph.LOGIN_ROUTE)
                            }
                        },
                    text = "Logout",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}