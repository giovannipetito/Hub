package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.Hyperlink

@Composable
fun HyperlinkScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.hyperlink),
    topics = listOf("Hyperlink")
) {
    Hyperlink(
        modifier = Modifier.padding(8.dp),
        fullText = "Welcome! Take a look at the source code of my app and come visit my LinkedIn profile.",
        fullTextColor = MaterialTheme.colorScheme.primary,
        linkText = listOf("source code", "LinkedIn profile"),
        hyperlinks = listOf(
            "https://github.com/giovannipetito/Hub",
            "https://www.linkedin.com/in/giovanni-petito-5919581b1/"
        ),
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
    )
}

@Preview(showBackground = true)
@Composable
fun HyperlinkScreenPreview() {
    HyperlinkScreen(navController = rememberNavController())
}