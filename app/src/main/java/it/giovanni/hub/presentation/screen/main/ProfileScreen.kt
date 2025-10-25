package it.giovanni.hub.presentation.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.navigation.routes.Auth
import it.giovanni.hub.navigation.routes.ProfileRoutes
import it.giovanni.hub.ui.items.buttons.MainTextButton
import it.giovanni.hub.utils.Constants.STATUS_BAR_HEIGHT

@Composable
fun ProfileScreen(navController: NavController) {

    // Get the SavedStateHandle from the NavController's current back stack entry
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // Retrieve the saved scroll position, or use default values if not available
    val initialScrollIndex = savedStateHandle?.get<Int>("scroll_index") ?: 0
    val initialScrollOffset = savedStateHandle?.get<Int>("scroll_offset") ?: 0

    // Create LazyListState and initialize with saved scroll position
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialScrollIndex,
        initialFirstVisibleItemScrollOffset = initialScrollOffset
    )

    // Save the scroll position when navigating away
    DisposableEffect(key1 = lazyListState) {
        onDispose {
            savedStateHandle?.set("scroll_index", lazyListState.firstVisibleItemIndex)
            savedStateHandle?.set("scroll_offset", lazyListState.firstVisibleItemScrollOffset)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = STATUS_BAR_HEIGHT)
    ) {
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.Detail1(id = 1, name = "Giovanni"))
                          },
                id = R.string.detail_1
            )
        }
        item {
            MainTextButton(
                onClick = {
                    val person = Person(
                        id = 3,
                        firstName = "Giovanni",
                        lastName = "Petito",
                        visibility = true
                    )
                    navController.currentBackStackEntry?.savedStateHandle?.set(key = "person", value = person)
                    navController.navigate(route = ProfileRoutes.Detail3)
                },
                id = R.string.detail_3
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.PersonState)
                },
                id = R.string.state_and_events
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = Auth)
                },
                id = R.string.auth_sign_up
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.Contacts)
                },
                id = R.string.contacts
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.Header)
                },
                id = R.string.header
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.StickyHeader)
                },
                id = R.string.sticky_header
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.SwipeActions)
                },
                id = R.string.swipe_actions
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.UsersCoroutines)
                },
                id = R.string.users_coroutines
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.UsersRxJava)
                },
                id = R.string.users_rxjava
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.PullToRefresh)
                },
                id = R.string.pull_to_refresh
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.Paging)
                },
                id = R.string.paging_3
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.SinglePermission)
                },
                id = R.string.single_permission
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.MultiplePermissions)
                },
                id = R.string.multiple_permissions
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.WebView)
                },
                id = R.string.web_view
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.CounterService)
                },
                id = R.string.counter_service
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.ErrorHandling)
                },
                id = R.string.error_handling
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.RoomCoroutines)
                },
                id = R.string.room_database_coroutines
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.RoomRxJava)
                },
                id = R.string.room_database_rxjava
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.Realtime)
                },
                id = R.string.realtime_database
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.Gemini)
                },
                id = R.string.gemini
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.ComfyUI)
                },
                id = R.string.text_to_image
            )
        }
        item {
            MainTextButton(
                onClick = {
                    navController.navigate(route = ProfileRoutes.TextToImageHistory)
                },
                id = R.string.text_to_image_history
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}