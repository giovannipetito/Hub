package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.model.GridItem
import it.giovanni.hub.ui.items.HorizontalGrid1
import it.giovanni.hub.ui.items.HorizontalGrid2
import it.giovanni.hub.ui.items.HorizontalGrid3
import it.giovanni.hub.ui.items.HorizontalGrid4
import it.giovanni.hub.ui.items.VerticalGrid1
import it.giovanni.hub.ui.items.VerticalGrid2
import it.giovanni.hub.ui.items.VerticalGrid3
import it.giovanni.hub.ui.items.VerticalGrid4
import it.giovanni.hub.ui.items.buttons.GridButton
import it.giovanni.hub.utils.Constants.getGridItems
import it.giovanni.hub.utils.GridType

@Composable
fun HubGridsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.grids),
    topics = listOf(
        "LazyVerticalGrid",
        "GridCells",
        "GridItemSpan",
        "LazyVerticalStaggeredGrid",
        "StaggeredGridCells"
    )
) { paddingValues ->
    val gridItems: List<GridItem> = getGridItems()

    var verticalGridsVisible by remember { mutableStateOf(true) }

    val grid: MutableState<GridType> = remember {
        mutableStateOf(GridType.VerticalGrid1)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            item {
                TextButton(
                    onClick = {
                        verticalGridsVisible = true
                        when (grid.value) {
                            GridType.VerticalGrid1 -> grid.value = GridType.VerticalGrid1
                            GridType.VerticalGrid2 -> grid.value = GridType.VerticalGrid2
                            GridType.VerticalGrid3 -> grid.value = GridType.VerticalGrid3
                            GridType.VerticalGrid4 -> grid.value = GridType.VerticalGrid4
                            GridType.HorizontalGrid1 -> grid.value = GridType.VerticalGrid1
                            GridType.HorizontalGrid2 -> grid.value = GridType.VerticalGrid2
                            GridType.HorizontalGrid3 -> grid.value = GridType.VerticalGrid3
                            GridType.HorizontalGrid4 -> grid.value = GridType.VerticalGrid4
                        }
                    }
                ) {
                    Text(text = "Vertical Grids")
                }

                TextButton(
                    onClick = {
                        verticalGridsVisible = false
                        when (grid.value) {
                            GridType.VerticalGrid1 -> grid.value = GridType.HorizontalGrid1
                            GridType.VerticalGrid2 -> grid.value = GridType.HorizontalGrid2
                            GridType.VerticalGrid3 -> grid.value = GridType.HorizontalGrid3
                            GridType.VerticalGrid4 -> grid.value = GridType.HorizontalGrid4
                            GridType.HorizontalGrid1 -> grid.value = GridType.HorizontalGrid1
                            GridType.HorizontalGrid2 -> grid.value = GridType.HorizontalGrid2
                            GridType.HorizontalGrid3 -> grid.value = GridType.HorizontalGrid3
                            GridType.HorizontalGrid4 -> grid.value = GridType.HorizontalGrid4
                        }
                    }
                ) {
                    Text(text = "Horizontal Grids")
                }
            }
        }

        if (verticalGridsVisible)
            ShowVerticalGrids(grid = grid)
        else
            ShowHorizontalGrids(grid = grid)

        when (grid.value) {
            GridType.VerticalGrid1 -> VerticalGrid1(gridItems, paddingValues)
            GridType.VerticalGrid2 -> VerticalGrid2(gridItems, paddingValues)
            GridType.VerticalGrid3 -> VerticalGrid3(gridItems, paddingValues)
            GridType.VerticalGrid4 -> VerticalGrid4(gridItems, paddingValues)
            GridType.HorizontalGrid1 -> HorizontalGrid1(gridItems, paddingValues)
            GridType.HorizontalGrid2 -> HorizontalGrid2(gridItems, paddingValues)
            GridType.HorizontalGrid3 -> HorizontalGrid3(gridItems, paddingValues)
            GridType.HorizontalGrid4 -> HorizontalGrid4(gridItems, paddingValues)
        }
    }
}

@Composable
fun ShowVerticalGrids(grid: MutableState<GridType>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        item {
            GridButton(grid = "Grid 1", onClick = { grid.value = GridType.VerticalGrid1 })
            GridButton(grid = "Grid 2", onClick = { grid.value = GridType.VerticalGrid2 })
            GridButton(grid = "Grid 3", onClick = { grid.value = GridType.VerticalGrid3 })
            GridButton(grid = "Grid 4", onClick = { grid.value = GridType.VerticalGrid4 })
        }
    }
}

@Composable
fun ShowHorizontalGrids(grid: MutableState<GridType>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        item {
            GridButton(grid = "Grid 1", onClick = { grid.value = GridType.HorizontalGrid1 })
            GridButton(grid = "Grid 2", onClick = { grid.value = GridType.HorizontalGrid2 })
            GridButton(grid = "Grid 3", onClick = { grid.value = GridType.HorizontalGrid3 })
            GridButton(grid = "Grid 4", onClick = { grid.value = GridType.HorizontalGrid4 })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GridScreenPreview() {
    HubGridsScreen(navController = rememberNavController())
}