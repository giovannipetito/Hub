package it.giovanni.hub.presentation.screen.main

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.navigation.navgraph.MainNavGraph
import it.giovanni.hub.presentation.viewmodel.MainViewModel
import it.giovanni.hub.ui.items.BottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        MainNavGraph(navController = navController, mainViewModel = mainViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController(), mainViewModel = hiltViewModel())
}