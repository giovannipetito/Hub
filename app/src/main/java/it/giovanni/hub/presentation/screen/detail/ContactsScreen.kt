package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person
import it.giovanni.hub.ui.items.HubCircularProgressIndicator
import it.giovanni.hub.ui.items.cards.ContactCard
import it.giovanni.hub.utils.Constants.mockedList
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun ContactsScreen(navController: NavController) {

    val topics: List<String> = listOf("LazyColumn", "random")

    val contacts: List<Person> = mockedList

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.contacts),
        topics = topics
    ) { paddingValues ->
        ShowContacts(contacts = contacts, paddingValues = paddingValues)
    }
}

@Composable
fun ShowContacts(contacts: List<Person>, paddingValues: PaddingValues) {
    LazyColumn(
        contentPadding = getContentPadding(paddingValues)
    ) {
        if (contacts.isEmpty()) {
            item {
                HubCircularProgressIndicator()
            }
        }

        items(
            items = contacts,
            key = { it.id }
        ) { contact: Person ->
            ContactCard(contact = contact)
            Spacer(modifier = Modifier.height(1.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsScreenPreview() {
    ContactsScreen(navController = rememberNavController())
}