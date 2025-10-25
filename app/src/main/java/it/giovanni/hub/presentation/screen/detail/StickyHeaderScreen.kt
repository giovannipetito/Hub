package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.ui.items.cards.PersonItem
import it.giovanni.hub.ui.items.cards.HubHeader
import it.giovanni.hub.utils.Constants
import it.giovanni.hub.utils.Globals.getContentPadding
import java.util.SortedMap

@Composable
fun StickyHeaderScreen(navController: NavController) {

    val topics: List<String> = listOf("stickyHeader")

    val contacts: SortedMap<Char, List<Person>> = Constants.mockedList.groupBy { it.lastName.first() }.toSortedMap()

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.sticky_header),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            contacts.forEach { (char, contactsByChar) ->
                stickyHeader {
                    HubHeader(text = char.toString())
                }
                items(contactsByChar) { person ->
                    PersonItem(person = person)
                    Spacer(modifier = Modifier.height(height = 1.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StickyHeaderScreenPreview() {
    StickyHeaderScreen(navController = rememberNavController())
}