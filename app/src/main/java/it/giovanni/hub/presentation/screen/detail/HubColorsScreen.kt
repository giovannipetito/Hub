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
import it.giovanni.hub.ui.items.TextItem
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
            TextItem(text = "primary", backgroundColor = MaterialTheme.colorScheme.primary)
            TextItem(text = "onPrimary", backgroundColor = MaterialTheme.colorScheme.onPrimary)
            TextItem(text = "primaryContainer", backgroundColor = MaterialTheme.colorScheme.primaryContainer)
            TextItem(text = "onPrimaryContainer", backgroundColor = MaterialTheme.colorScheme.onPrimaryContainer)
            TextItem(text = "secondary", backgroundColor = MaterialTheme.colorScheme.secondary)
            TextItem(text = "onSecondary", backgroundColor = MaterialTheme.colorScheme.onSecondary)
            TextItem(text = "secondaryContainer", backgroundColor = MaterialTheme.colorScheme.secondaryContainer)
            TextItem(text = "onSecondaryContainer", backgroundColor = MaterialTheme.colorScheme.onSecondaryContainer)
            TextItem(text = "tertiary", backgroundColor = MaterialTheme.colorScheme.tertiary)
            TextItem(text = "onTertiary", backgroundColor = MaterialTheme.colorScheme.onTertiary)
            TextItem(text = "tertiaryContainer", backgroundColor = MaterialTheme.colorScheme.tertiaryContainer)
            TextItem(text = "onTertiaryContainer", backgroundColor = MaterialTheme.colorScheme.onTertiaryContainer)
            TextItem(text = "error", backgroundColor = MaterialTheme.colorScheme.error)
            TextItem(text = "errorContainer", backgroundColor = MaterialTheme.colorScheme.errorContainer)
            TextItem(text = "onError", backgroundColor = MaterialTheme.colorScheme.onError)
            TextItem(text = "onErrorContainer", backgroundColor = MaterialTheme.colorScheme.onErrorContainer)
            TextItem(text = "background", backgroundColor = MaterialTheme.colorScheme.background)
            TextItem(text = "onBackground", backgroundColor = MaterialTheme.colorScheme.onBackground)
            TextItem(text = "surface", backgroundColor = MaterialTheme.colorScheme.surface)
            TextItem(text = "onSurface", backgroundColor = MaterialTheme.colorScheme.onSurface)
            TextItem(text = "surfaceVariant", backgroundColor = MaterialTheme.colorScheme.surfaceVariant)
            TextItem(text = "onSurfaceVariant", backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant)
            TextItem(text = "outline", backgroundColor = MaterialTheme.colorScheme.outline)
            TextItem(text = "inverseOnSurface", backgroundColor = MaterialTheme.colorScheme.inverseOnSurface)
            TextItem(text = "inverseSurface", backgroundColor = MaterialTheme.colorScheme.inverseSurface)
            TextItem(text = "inversePrimary", backgroundColor = MaterialTheme.colorScheme.inversePrimary)
            TextItem(text = "surfaceTint", backgroundColor = MaterialTheme.colorScheme.surfaceTint)
            TextItem(text = "outlineVariant", backgroundColor = MaterialTheme.colorScheme.outlineVariant)
            TextItem(text = "scrim", backgroundColor = MaterialTheme.colorScheme.scrim)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubColorsScreenPreview() {
    HubColorsScreen(navController = rememberNavController())
}