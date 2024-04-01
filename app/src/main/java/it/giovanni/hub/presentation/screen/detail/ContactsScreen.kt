package it.giovanni.hub.presentation.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.ui.items.HubCircularProgressIndicator
import it.giovanni.hub.ui.items.buttons.HubButton
import it.giovanni.hub.ui.items.cards.PersonItem
import it.giovanni.hub.utils.Constants.NAVIGATION_BAR_HEIGHT
import it.giovanni.hub.utils.Constants.mockedList
import it.giovanni.hub.utils.Globals.getExtraContentPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val contactCardHeight = 64.dp

@Composable
fun ContactsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.contacts),
    topics = listOf(
        "LazyColumn",
        "random",
        "rememberLazyListState",
        "rememberCoroutineScope",
        "derivedStateOf",
        "AnimatedVisibility",
        "LaunchedEffect",
        "snapshotFlow",
    )
) { paddingValues ->

    val contacts: List<Person> = mockedList

    val lazyListState: LazyListState = rememberLazyListState()

    // Remember a CoroutineScope to be able to launch.
    val scope: CoroutineScope = rememberCoroutineScope()

    ShowContacts(
        contacts = contacts,
        lazyListState = lazyListState,
        scope = scope,
        paddingValues = paddingValues
    )
}

@Composable
fun ShowContacts(
    contacts: List<Person>,
    lazyListState: LazyListState,
    scope: CoroutineScope,
    paddingValues: PaddingValues
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = getExtraContentPadding(
            paddingValues = paddingValues,
            extraPadding = contactCardHeight
        )
    ) {
        if (contacts.isEmpty()) {
            item {
                HubCircularProgressIndicator()
            }
        } else {
            item {
                Spacer(modifier = Modifier.height(1.dp))
            }
        }

        items(
            items = contacts,
            key = { it.id }
        ) { person: Person ->
            PersonItem(person = person)
            Spacer(modifier = Modifier.height(1.dp))
        }
    }

    // Show the button if the first visible item is past the first item.
    // We use derivedStateOf to minimize unnecessary compositions.
    val showButton: Boolean by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }

    ScrollToTop(showButton = showButton, scope = scope, lazyListState = lazyListState)

    // Reading the state directly in composition is useful when you need to update other UI composable,
    // but there are also scenarios where the event does not need to be handled in the same composition.
    // A common example of this is sending an analytics event once the user has scrolled past a certain
    // point. To handle this efficiently, we can use a snapshotFlow().
    LaunchedEffect(key1 = lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .map { index -> index > 0 }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                // HubAnalyticsService.sendScrolledPastFirstItemEvent()
            }
    }
}

@Composable
fun ScrollToTop(showButton: Boolean, scope: CoroutineScope, lazyListState: LazyListState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = NAVIGATION_BAR_HEIGHT),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = showButton
        ) {
            HubButton(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "Scroll To Top",
                onClick = {
                    scope.launch {
                        // Animate scroll to the first item.
                        lazyListState.animateScrollToItem(index = 0)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsScreenPreview() {
    ContactsScreen(navController = rememberNavController())
}