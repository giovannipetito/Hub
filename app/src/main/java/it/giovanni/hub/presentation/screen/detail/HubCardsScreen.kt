package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.cards.ScrollableCard
import it.giovanni.hub.ui.items.cards.AnimatedBorderCard
import it.giovanni.hub.ui.items.cards.ExpandableCard
import it.giovanni.hub.ui.items.cards.SelectableCard
import it.giovanni.hub.utils.Constants

@Composable
fun HubCardsScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "Box",
        "verticalScroll",
        "ExpandableCard",
        "SelectableCard",
        "Job",
        "AnimatedBorderCard",
        "rememberInfiniteTransition"
    )

    var selected1: Boolean by remember { mutableStateOf(false) }
    var selected2: Boolean by remember { mutableStateOf(false) }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.cards),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding()),
            ) {
            item {
                ScrollableCard()

                ExpandableCard(
                    modifier = Modifier.padding(all = 12.dp),
                    title = "Expandable Card"
                )

                SelectableCard(
                    modifier = Modifier.padding(all = 12.dp),
                    selected = selected1,
                    title = "Selectable Card 1",
                    onClick = {
                        selected1 = !selected1
                    }
                )

                SelectableCard(
                    modifier = Modifier.padding(all = 12.dp),
                    selected = selected2,
                    title = "Selectable Card 2",
                    subtitle = Constants.loremIpsumShortText,
                    onClick = {
                        selected2 = !selected2
                    }
                )

                AnimatedBorderCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    gradient = Brush.sweepGradient(listOf(Color.Magenta, Color.Cyan)),
                    onCardClick = {}
                ) {
                    AnimatedBorderCardContent()
                }

                AnimatedBorderCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 12.dp),
                    shape = RoundedCornerShape(8.dp),
                    borderWidth = 1.dp,
                    gradient = Brush.linearGradient(listOf(Color.Magenta, Color.Cyan)),
                    onCardClick = {}
                ) {
                    AnimatedBorderCardContent()
                }
            }
        }
    }
}

@Composable
fun AnimatedBorderCardContent() {
    Column(modifier = Modifier.padding(all = 24.dp)) {

        Text(
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontWeight = FontWeight.Bold,
            text = "Welcome"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Normal,
            text = Constants.loremIpsumShortText
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HubBoxesScreenPreview() {
    HubCardsScreen(navController = rememberNavController())
}