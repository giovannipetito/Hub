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
import it.giovanni.hub.ui.items.HubProgressIndicator
import it.giovanni.hub.ui.items.cards.PersonItem
import it.giovanni.hub.ui.items.cards.HubHeader
import it.giovanni.hub.utils.Constants.mockedList
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun StickyHeaderScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.sticky_header),
    topics = listOf("LazyColumn", "stickyHeader", "groupBy", "random")
) { paddingValues ->
    val contacts: List<Person> = mockedList

    val groupedContacts = contacts.groupBy { it.lastName[0] }
    ShowStickyHeaderContacts(groupedContacts = groupedContacts, paddingValues = paddingValues)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowStickyHeaderContacts(groupedContacts: Map<Char, List<Person>>, paddingValues: PaddingValues) {
    LazyColumn(
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        if (groupedContacts.isEmpty()) {
            item {
                HubProgressIndicator()
            }
        }

        groupedContacts.forEach { (firstLetter, contactsGroupedByFirstLetter) ->
            stickyHeader {
                HubHeader(text = firstLetter.toString())
            }

            items(contactsGroupedByFirstLetter) { person ->
                PersonItem(person = person)
                Spacer(modifier = Modifier.height(height = 1.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StickyHeaderScreenPreview() {
    StickyHeaderScreen(navController = rememberNavController())
}