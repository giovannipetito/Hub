package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.ExperimentalFoundationApi
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
import it.giovanni.hub.ui.items.cards.ContactCard
import it.giovanni.hub.ui.items.cards.HubHeader
import it.giovanni.hub.utils.Constants.getMockedContacts
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun HeaderScreen(navController: NavController) {

    val topics: List<String> = listOf("LazyColumn", "stickyHeader", "random")

    val contacts: List<Person> = getMockedContacts()

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.header),
        topics = topics
    ) { paddingValues ->
        ShowHeaderContacts(contacts = contacts, paddingValues = paddingValues)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowHeaderContacts(contacts: List<Person>, paddingValues: PaddingValues) {
    LazyColumn(
        contentPadding = getContentPadding(paddingValues)
    ) {
        stickyHeader {
            HubHeader(text = "Header")
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
fun HeaderScreenPreview() {
    HeaderScreen(navController = rememberNavController())
}