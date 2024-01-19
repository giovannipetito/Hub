package it.giovanni.hub.presentation.screen.detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.FontText
import kotlinx.coroutines.CoroutineExceptionHandler

@Composable
fun HubFontsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.fonts),
    topics = listOf(
        "GoogleFont.Provider",
        "fontName",
        "fontFamily",
        "fallback font",
        "Typography"
    )
) {
    val handler = CoroutineExceptionHandler { _, throwable ->
        // Process the Throwable.
        Log.e("[FONT]", "There has been an issue: ", throwable)
    }

    CompositionLocalProvider(
        LocalFontFamilyResolver provides createFontFamilyResolver(LocalContext.current, handler)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                FontText(text = "displayLarge", fontSize = MaterialTheme.typography.displayLarge.fontSize)
                FontText(text = "displayMedium", fontSize = MaterialTheme.typography.displayMedium.fontSize)
                FontText(text = "displaySmall", fontSize = MaterialTheme.typography.displaySmall.fontSize)
                FontText(text = "headlineLarge", fontSize = MaterialTheme.typography.headlineLarge.fontSize)
                FontText(text = "headlineMedium", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
                FontText(text = "headlineSmall", fontSize = MaterialTheme.typography.headlineSmall.fontSize)
                FontText(text = "titleLarge", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                FontText(text = "titleMedium", fontSize = MaterialTheme.typography.titleMedium.fontSize)
                FontText(text = "titleSmall", fontSize = MaterialTheme.typography.titleSmall.fontSize)
                FontText(text = "bodyLarge", fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                FontText(text = "bodyMedium", fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                FontText(text = "bodySmall", fontSize = MaterialTheme.typography.bodySmall.fontSize)
                FontText(text = "labelLarge", fontSize = MaterialTheme.typography.labelLarge.fontSize)
                FontText(text = "labelMedium", fontSize = MaterialTheme.typography.labelMedium.fontSize)
                FontText(text = "labelSmall", fontSize = MaterialTheme.typography.labelSmall.fontSize)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubFontsScreenPreview() {
    HubFontsScreen(navController = rememberNavController())
}