package it.giovanni.hub.presentation.screen.detail

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.ui.items.HubCircularProgressIndicator
import it.giovanni.hub.ui.items.cards.SwipeableActionsCard
import it.giovanni.hub.utils.Constants.mockedList
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun SwipeableActionsScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "LazyColumn",
        "random"
    )

    val contacts: List<Person> = mockedList

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.swipeable_actions),
        topics = topics
    ) { paddingValues ->
        ShowSwipeableContacts(
            contacts = contacts,
            paddingValues = paddingValues
        )
    }
}

@Composable
fun ShowSwipeableContacts(
    contacts: List<Person>,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    LazyColumn(
        contentPadding = getContentPadding(paddingValues = paddingValues)
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
        ) { contact: Person ->
            SwipeableActionsCard(
                contact = contact,
                onSwipe = {
                    Toast.makeText(context, contact.firstName, Toast.LENGTH_SHORT).show()
                },
                onIconClick = {
                    Toast.makeText(context, contact.lastName, Toast.LENGTH_SHORT).show()
                }
            )
            Spacer(modifier = Modifier.height(1.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SwipeableActionScreenPreview() {
    SwipeableActionsScreen(navController = rememberNavController())
}