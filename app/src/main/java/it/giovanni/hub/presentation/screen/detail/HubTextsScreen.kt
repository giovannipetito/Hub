package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.HubTextsViewModel
import it.giovanni.hub.ui.items.CapitalText
import it.giovanni.hub.ui.items.DisableSelectionText
import it.giovanni.hub.ui.items.MarqueeText
import it.giovanni.hub.ui.items.ScriptText
import it.giovanni.hub.ui.items.Text1
import it.giovanni.hub.ui.items.Text2
import it.giovanni.hub.ui.items.Text3
import it.giovanni.hub.ui.items.TextHorizontalAnimation
import it.giovanni.hub.ui.items.TextVerticalAnimation
import it.giovanni.hub.utils.Constants
import it.giovanni.hub.utils.Globals.hexColor

@Composable
fun HubTextsScreen(navController: NavController) {

    val topics: List<String> = listOf(
        "Text",
        "stringResource",
        "maxLines",
        "Ellipsis",
        "buildAnnotatedString",
        "SelectionContainer",
        "buildAnnotatedString",
        "append",
        "AnimatedContent",
        "basicMarquee"
    )

    val viewModel: HubTextsViewModel = viewModel()
    val seconds: Any by viewModel.seconds.collectAsState(initial = "00")

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.texts),
        topics = topics
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                Text1()

                Spacer(modifier = Modifier.height(12.dp))

                Text2(modifier = Modifier, text = "Hello, World!")

                Spacer(modifier = Modifier.height(12.dp))

                Text3()

                Spacer(modifier = Modifier.height(12.dp))

                CapitalText(color = MaterialTheme.colorScheme.primary)

                Spacer(modifier = Modifier.height(12.dp))

                DisableSelectionText()

                Spacer(modifier = Modifier.height(12.dp))

                ScriptText(
                    normalText = "Giovanni",
                    normalFontSize = MaterialTheme.typography.titleMedium.fontSize,
                    scriptText = "Petito",
                    scriptTextColor = MaterialTheme.colorScheme.primary,
                    scriptTextFontSize = MaterialTheme.typography.titleMedium.fontSize,
                    scriptTextFontWeight = FontWeight.Normal,
                    scriptTextBaselineShift = BaselineShift.Superscript,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextVerticalAnimation(seconds = seconds, slideOutVertically = true)
                    TextVerticalAnimation(seconds = seconds, slideOutVertically = false)
                    TextHorizontalAnimation(seconds = seconds, slideOutHorizontally = true)
                    TextHorizontalAnimation(seconds = seconds, slideOutHorizontally = false)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier
                        .background(color = hexColor)
                        .padding(all = 4.dp),
                    text = "Hex Color Code",
                    style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                    textAlign = TextAlign.Center,
                    color = "#2B3E97".hexColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                MarqueeText(text = Constants.loremIpsumShortText)

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubTextsScreenPreview() {
    HubTextsScreen(navController = rememberNavController())
}