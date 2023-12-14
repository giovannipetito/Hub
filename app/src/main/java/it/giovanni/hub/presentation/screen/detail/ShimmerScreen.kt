package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.Shimmer

@Composable
fun ShimmerScreen(navController: NavController) {

    val topics: List<String> = listOf("rememberInfiniteTransition", "Brush",)

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.shimmer_items),
        topics = topics
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column {
                repeat(6) {
                    Shimmer()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmerScreenPreview() {
    ShimmerScreen(navController = rememberNavController())
}