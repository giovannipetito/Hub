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
import it.giovanni.hub.domain.model.Contact
import it.giovanni.hub.ui.items.HubProgressIndicator
import it.giovanni.hub.ui.items.cards.SwipeActionsItem
import it.giovanni.hub.utils.Constants.mockedContacts
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun SwipeActionsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.swipe_actions),
    topics = listOf("Swipe Actions", "BoxWithConstraints")
) { paddingValues ->
    val contacts: List<Contact> = mockedContacts
    ShowSwipeContacts(contacts = contacts, paddingValues = paddingValues)
}

@Composable
fun ShowSwipeContacts(
    contacts: List<Contact>,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    LazyColumn(
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        if (contacts.isEmpty()) {
            item {
                HubProgressIndicator()
            }
        } else {
            item {
                Spacer(modifier = Modifier.height(height = 1.dp))
            }
        }

        items(
            items = contacts,
            key = { it.id }
        ) { contact: Contact ->
            SwipeActionsItem(
                contact = contact,
                onSwipe = { actionName ->
                    Toast.makeText(context, actionName + " " + contact.firstName, Toast.LENGTH_SHORT).show()
                },
                onIconClick = { actionName ->
                    Toast.makeText(context, actionName + " " + contact.firstName, Toast.LENGTH_SHORT).show()
                }
            )
            Spacer(modifier = Modifier.height(height = 1.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SwipeActionsScreenPreview() {
    SwipeActionsScreen(navController = rememberNavController())
}