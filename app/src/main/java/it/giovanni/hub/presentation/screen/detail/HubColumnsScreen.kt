package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.ui.items.buttons.AlignmentColumnButton
import it.giovanni.hub.ui.items.buttons.ArrangementButton
import it.giovanni.hub.ui.items.Column1
import it.giovanni.hub.ui.items.Column2
import it.giovanni.hub.ui.items.buttons.ColumnButton
import it.giovanni.hub.ui.items.DescriptionText
import it.giovanni.hub.utils.ColumnType

@Composable
fun HubColumnsScreen(navController: NavController) {

    val alignment: MutableState<Alignment.Horizontal> = remember {
        mutableStateOf(Alignment.CenterHorizontally)
    }

    val arrangement: MutableState<Arrangement.HorizontalOrVertical> = remember {
        mutableStateOf(Arrangement.Center)
    }

    val column: MutableState<ColumnType> = remember {
        mutableStateOf(ColumnType.Column1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        DescriptionText(description = "Columns:")
        LazyRow(
            horizontalArrangement = Arrangement.Center
        ) {
            item {
                ColumnButton(column = column, type = ColumnType.Column1)
                ColumnButton(column = column, type = ColumnType.Column2)
            }
        }
        DescriptionText(description = "Alignment:")
        LazyRow(
            horizontalArrangement = Arrangement.Center
        ) {
            item {
                AlignmentColumnButton(alignment = alignment, horizontal = Alignment.Start, name = "Start")
                AlignmentColumnButton(alignment = alignment, horizontal = Alignment.CenterHorizontally, name = "CenterHorizontally")
                AlignmentColumnButton(alignment = alignment, horizontal = Alignment.End, name = "End")
            }
        }
        DescriptionText(description = "Arrangement:")
        LazyRow(
            horizontalArrangement = Arrangement.Center
        ) {
            item {
                ArrangementButton(arrangement = arrangement, Arrangement.Center)
                ArrangementButton(arrangement = arrangement, Arrangement.SpaceEvenly)
                ArrangementButton(arrangement = arrangement, Arrangement.SpaceAround)
                ArrangementButton(arrangement = arrangement, Arrangement.SpaceBetween)
                ArrangementButton(arrangement = arrangement, Arrangement.spacedBy(12.dp))
            }
        }

        when (column.value) {
            ColumnType.Column1 -> Column1(alignment = alignment.value, arrangement = arrangement.value)
            ColumnType.Column2 -> Column2(alignment = alignment.value, arrangement = arrangement.value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubColumnsScreenPreview() {
    HubColumnsScreen(navController = rememberNavController())
}