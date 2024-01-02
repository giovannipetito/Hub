package it.giovanni.hub.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import it.giovanni.hub.data.model.GridItem
import it.giovanni.hub.ui.items.cards.HubHeader
import it.giovanni.hub.utils.Globals

@Composable
fun VerticalGrid1(gridItems: List<GridItem>, paddingValues: PaddingValues) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = Globals.getContentPadding(paddingValues)
    ) {
        items(gridItems.size) { index ->
            GridItemImage(gridItems[index])
        }
    }
}

@Composable
fun VerticalGrid2(gridItems: List<GridItem>, paddingValues: PaddingValues) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = Globals.getContentPadding(paddingValues)
    ) {
        item(span = {
            GridItemSpan(maxLineSpan)
        }) {
            HubHeader(text = "Header")
        }
        items(gridItems.size) { index ->
            GridItemImage(gridItems[index])
        }
    }
}

@Composable
fun VerticalGrid3(gridItems: List<GridItem>, paddingValues: PaddingValues) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(200.dp),
        contentPadding = Globals.getContentPadding(paddingValues),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(gridItems.size) { index ->
                AsyncGridItemImage(gridItems[index])
            }
        }
    )
}

@Composable
fun VerticalGrid4(gridItems: List<GridItem>, paddingValues: PaddingValues) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        columns = StaggeredGridCells.Fixed(3),
        contentPadding = Globals.getContentPadding(paddingValues),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(gridItems.size) { index ->
                AsyncGridItemImage(gridItems[index])
            }
        }
    )
}

@Composable
fun HorizontalGrid1(gridItems: List<GridItem>, paddingValues: PaddingValues) {
    LazyHorizontalGrid(
        modifier = Modifier.fillMaxSize(),
        rows = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = Globals.getContentPadding(paddingValues)
    ) {
        items(gridItems.size) { index ->
            GridItemImage(gridItems[index])
        }
    }
}

@Composable
fun HorizontalGrid2(gridItems: List<GridItem>, paddingValues: PaddingValues) {
    LazyHorizontalGrid(
        modifier = Modifier.fillMaxSize(),
        rows = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = Globals.getContentPadding(paddingValues)
    ) {
        item(span = {
            GridItemSpan(maxLineSpan)
        }) {
            HubHeader(text = "Header")
        }
        items(gridItems.size) { index ->
            GridItemImage(gridItems[index])
        }
    }
}

@Composable
fun HorizontalGrid3(gridItems: List<GridItem>, paddingValues: PaddingValues) {
    LazyHorizontalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        rows = StaggeredGridCells.Adaptive(200.dp),
        contentPadding = Globals.getContentPadding(paddingValues),
        horizontalItemSpacing = 4.dp,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(gridItems.size) { index ->
                AsyncGridItemImage(gridItems[index])
            }
        }
    )
}

@Composable
fun HorizontalGrid4(gridItems: List<GridItem>, paddingValues: PaddingValues) {
    LazyHorizontalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        rows = StaggeredGridCells.Fixed(3),
        contentPadding = Globals.getContentPadding(paddingValues),
        horizontalItemSpacing = 4.dp,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(gridItems.size) { index ->
                AsyncGridItemImage(gridItems[index])
            }
        }
    )
}

@Composable
fun GridItemImage(gridItem: GridItem) {
    Image(
        modifier = Modifier
            .size(156.dp)
            .clip(RoundedCornerShape(4.dp)),
        painter = rememberAsyncImagePainter(gridItem.imageUrl),
        contentDescription = "GridItem with ID ${gridItem.id}"
    )
}

@Composable
fun AsyncGridItemImage(gridItem: GridItem) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        model = gridItem.imageUrl,
        contentScale = ContentScale.Crop,
        contentDescription = "GridItem with ID ${gridItem.id}"
    )
}