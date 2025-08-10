package it.giovanni.hub.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import it.giovanni.hub.navigation.routes.Settings
import it.giovanni.hub.navigation.routes.BottomBarRoutes
import it.giovanni.hub.navigation.routes.SettingsRoutes
import it.giovanni.hub.presentation.screen.detail.AlertBarScreen
import it.giovanni.hub.presentation.screen.detail.DateTimeScreen
import it.giovanni.hub.presentation.screen.detail.DragAndDropScreen
import it.giovanni.hub.presentation.screen.detail.HorizontalPagerScreen
import it.giovanni.hub.presentation.screen.detail.HubButtonsScreen
import it.giovanni.hub.presentation.screen.detail.HubCardsScreen
import it.giovanni.hub.presentation.screen.detail.HubColorsScreen
import it.giovanni.hub.presentation.screen.detail.HubColumnsScreen
import it.giovanni.hub.presentation.screen.detail.HubFontsScreen
import it.giovanni.hub.presentation.screen.detail.HubGridsScreen
import it.giovanni.hub.presentation.screen.detail.HubRowsScreen
import it.giovanni.hub.presentation.screen.detail.HubTextsScreen
import it.giovanni.hub.presentation.screen.detail.ShimmerScreen
import it.giovanni.hub.presentation.screen.detail.ShuffledScreen
import it.giovanni.hub.presentation.screen.detail.HubTextFieldsScreen
import it.giovanni.hub.presentation.screen.detail.PaneScreen
import it.giovanni.hub.presentation.screen.detail.PhotoPickerScreen
import it.giovanni.hub.presentation.screen.detail.SliderScreen
import it.giovanni.hub.presentation.screen.detail.SpeechToTextScreen
import it.giovanni.hub.presentation.screen.detail.TextToSpeechScreen
import it.giovanni.hub.presentation.screen.detail.UIScreen
import it.giovanni.hub.presentation.screen.main.SettingsScreen
import it.giovanni.hub.presentation.viewmodel.MainViewModel

fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    navigation(
        route = Settings.toString(),
        startDestination = BottomBarRoutes.Settings.route
    ) {
        composable(
            route = BottomBarRoutes.Settings.route
        ) {
            SettingsScreen(navController = navController)
        }

        composable<SettingsRoutes.Colors> {
            HubColorsScreen(navController = navController)
        }

        composable<SettingsRoutes.Fonts> {
            HubFontsScreen(navController = navController)
        }

        composable<SettingsRoutes.Buttons> {
            HubButtonsScreen(navController = navController)
        }

        composable<SettingsRoutes.Columns> {
            HubColumnsScreen(navController = navController)
        }

        composable<SettingsRoutes.Rows> {
            HubRowsScreen(navController = navController)
        }

        composable<SettingsRoutes.Grids> {
            HubGridsScreen(navController = navController)
        }

        composable<SettingsRoutes.Texts> {
            HubTextsScreen(navController = navController)
        }

        composable<SettingsRoutes.TextFields> {
            HubTextFieldsScreen(navController = navController)
        }

        composable<SettingsRoutes.Cards> {
            HubCardsScreen(navController = navController)
        }

        composable<SettingsRoutes.UI> {
            UIScreen(navController = navController)
        }

        composable<SettingsRoutes.Slider> {
            SliderScreen(navController = navController)
        }

        composable<SettingsRoutes.PhotoPicker> {
            PhotoPickerScreen(navController = navController)
        }

        composable<SettingsRoutes.Shimmer> {
            ShimmerScreen(navController = navController)
        }

        composable<SettingsRoutes.Shuffled> {
            ShuffledScreen(navController = navController)
        }

        topAppBarsNavGraph(navController = navController)

        composable<SettingsRoutes.HorizontalPager> {
            HorizontalPagerScreen(navController = navController)
        }

        composable<SettingsRoutes.AlertBar> {
            AlertBarScreen(navController = navController)
        }

        composable<SettingsRoutes.TextToSpeech> {
            TextToSpeechScreen(navController = navController)
        }

        composable<SettingsRoutes.SpeechToText> {
            SpeechToTextScreen(navController = navController)
        }

        composable<SettingsRoutes.DragAndDrop> {
            DragAndDropScreen(navController = navController)
        }

        composable<SettingsRoutes.DateTime> {
            DateTimeScreen(navController = navController)
        }

        composable<SettingsRoutes.Pane> {
            PaneScreen(navController = navController)
        }
    }
}