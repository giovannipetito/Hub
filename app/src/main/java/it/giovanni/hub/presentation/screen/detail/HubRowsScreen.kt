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
import it.giovanni.hub.ui.items.AlignmentRowButton
import it.giovanni.hub.ui.items.ArrangementButton
import it.giovanni.hub.ui.items.DescriptionText
import it.giovanni.hub.ui.items.Row1
import it.giovanni.hub.ui.items.Row2
import it.giovanni.hub.ui.items.RowButton
import it.giovanni.hub.utils.RowType

@Composable
fun HubRowsScreen(navController: NavController) {

    val alignment: MutableState<Alignment.Vertical> = remember {
        mutableStateOf(Alignment.CenterVertically)
    }

    val arrangement: MutableState<Arrangement.HorizontalOrVertical> = remember {
        mutableStateOf(Arrangement.Center)
    }

    val row: MutableState<RowType> = remember {
        mutableStateOf(RowType.Row1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        DescriptionText(description = "Rows:")
        LazyRow(
            horizontalArrangement = Arrangement.Center
        ) {
            item {
                RowButton(row = row, type = RowType.Row1)
                RowButton(row = row, type = RowType.Row2)
            }
        }
        DescriptionText(description = "Alignment:")
        LazyRow(
            horizontalArrangement = Arrangement.Center
        ) {
            item {
                AlignmentRowButton(alignment = alignment, vertical = Alignment.Top, name = "Top")
                AlignmentRowButton(alignment = alignment, vertical = Alignment.CenterVertically, name = "CenterVertically")
                AlignmentRowButton(alignment = alignment, vertical = Alignment.Bottom, name = "Bottom")
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

        when (row.value) {
            RowType.Row1 -> Row1(alignment = alignment.value, arrangement = arrangement.value)
            RowType.Row2 -> Row2(alignment = alignment.value, arrangement = arrangement.value)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubRowsScreenPreview() {
    HubRowsScreen(navController = rememberNavController())
}