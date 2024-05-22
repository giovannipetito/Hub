package it.giovanni.hub.presentation.screen.detail

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import it.giovanni.hub.R
import it.giovanni.hub.utils.Constants.getPhotos
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun HorizontalPagerScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.horizontal_pager),
    topics = listOf("HorizontalPager", "AsyncImage", "SnapshotStateList", "rememberPagerState")
) {
    val photos: List<String> = getPhotos()

    val pagerState = rememberPagerState(pageCount = { photos.size })

    var alpha = remember { 1f }

    HorizontalPager(
        state = pagerState,
        contentPadding = getContentPadding(paddingValues = it)
    ) { index ->
        val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
        val imageSize by animateFloatAsState(
            targetValue = if (pageOffset != 0.0f) 0.75f else 1f,
            animationSpec = tween(durationMillis = 300),
            label = "Image size"
        )

        val configuration: Configuration = LocalConfiguration.current
        val orientation: Int = configuration.orientation

        val contentScale: ContentScale = if (orientation == Configuration.ORIENTATION_PORTRAIT)
            ContentScale.Crop
        else
            ContentScale.Fit

        LaunchedEffect(key1 = imageSize) {
            alpha = if (pageOffset != 0.0f) 0.5f else 1f
        }

        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp)
                .graphicsLayer {
                    scaleX = imageSize
                    scaleY = imageSize
                }
                .clip(RoundedCornerShape(16.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(photos[index])
                .build(),
            contentDescription ="Photo",
            contentScale = contentScale,
            alpha = alpha
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HorizontalPagerScreenPreview() {
    HorizontalPagerScreen(navController = rememberNavController())
}