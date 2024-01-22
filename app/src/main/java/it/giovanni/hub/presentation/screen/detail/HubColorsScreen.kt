package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.Text2
import it.giovanni.hub.utils.Globals

@Composable
fun HubColorsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.colors),
    topics = listOf(
        "Color",
        "ColorScheme",
        "lightColorScheme",
        "darkColorScheme"
    )
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = Globals.getContentPadding(paddingValues = it)
    ) {
        item {
            Text2(text = "primary", backgroundColor = MaterialTheme.colorScheme.primary)
            Text2(text = "onPrimary", backgroundColor = MaterialTheme.colorScheme.onPrimary)
            Text2(text = "primaryContainer", backgroundColor = MaterialTheme.colorScheme.primaryContainer)
            Text2(text = "onPrimaryContainer", backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer)
            Text2(text = "secondary", backgroundColor = MaterialTheme.colorScheme.secondary)
            Text2(text = "onSecondary", backgroundColor = MaterialTheme.colorScheme.onSecondary)
            Text2(text = "secondaryContainer", backgroundColor = MaterialTheme.colorScheme.secondaryContainer)
            Text2(text = "onSecondaryContainer", backgroundColor = MaterialTheme.colorScheme.onSecondaryContainer)
            Text2(text = "tertiary", backgroundColor = MaterialTheme.colorScheme.tertiary)
            Text2(text = "onTertiary", backgroundColor = MaterialTheme.colorScheme.onTertiary)
            Text2(text = "tertiaryContainer", backgroundColor = MaterialTheme.colorScheme.tertiaryContainer)
            Text2(text = "onTertiaryContainer", backgroundColor = MaterialTheme.colorScheme.onTertiaryContainer)
            Text2(text = "error", backgroundColor = MaterialTheme.colorScheme.error)
            Text2(text = "errorContainer", backgroundColor = MaterialTheme.colorScheme.errorContainer)
            Text2(text = "onError", backgroundColor = MaterialTheme.colorScheme.onError)
            Text2(text = "onErrorContainer", backgroundColor = MaterialTheme.colorScheme.onErrorContainer)
            Text2(text = "background", backgroundColor = MaterialTheme.colorScheme.background)
            Text2(text = "onBackground", backgroundColor = MaterialTheme.colorScheme.onBackground)
            Text2(text = "surface", backgroundColor = MaterialTheme.colorScheme.surface)
            Text2(text = "onSurface", backgroundColor = MaterialTheme.colorScheme.onSurface)
            Text2(text = "surfaceVariant", backgroundColor = MaterialTheme.colorScheme.surfaceVariant)
            Text2(text = "onSurfaceVariant", backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant)
            Text2(text = "outline", backgroundColor = MaterialTheme.colorScheme.outline)
            Text2(text = "inverseOnSurface", backgroundColor = MaterialTheme.colorScheme.inverseOnSurface)
            Text2(text = "inverseSurface", backgroundColor = MaterialTheme.colorScheme.inverseSurface)
            Text2(text = "inversePrimary", backgroundColor = MaterialTheme.colorScheme.inversePrimary)
            Text2(text = "surfaceTint", backgroundColor = MaterialTheme.colorScheme.surfaceTint)
            Text2(text = "outlineVariant", backgroundColor = MaterialTheme.colorScheme.outlineVariant)
            Text2(text = "scrim", backgroundColor = MaterialTheme.colorScheme.scrim)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubColorsScreenPreview() {
    HubColorsScreen(navController = rememberNavController())
}