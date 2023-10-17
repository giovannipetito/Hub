package it.giovanni.hub.screens.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.MainActivity
import it.giovanni.hub.screens.detail.ProfileScreen
import it.giovanni.hub.screens.detail.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination= BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController = rememberNavController(), mainActivity = MainActivity())
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(navController = rememberNavController(), mainActivity = MainActivity())
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(navController = rememberNavController(), mainActivity = MainActivity())
        }
    }
}