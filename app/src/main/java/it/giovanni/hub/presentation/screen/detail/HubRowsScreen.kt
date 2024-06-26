package it.giovanni.hub.presentation.screen.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.buttons.AlignmentRowButton
import it.giovanni.hub.ui.items.buttons.ArrangementButton
import it.giovanni.hub.ui.items.Row1
import it.giovanni.hub.ui.items.Row2
import it.giovanni.hub.ui.items.buttons.RowButton
import it.giovanni.hub.utils.RowType

@Composable
fun HubRowsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.rows),
    topics = listOf("Row", "verticalAlignment", "horizontalArrangement", "RowScope")
) {
    val alignment: MutableState<Alignment.Vertical> = remember {
        mutableStateOf(Alignment.CenterVertically)
    }

    val arrangement: MutableState<Arrangement.HorizontalOrVertical> = remember {
        mutableStateOf(Arrangement.Center)
    }

    val row: MutableState<RowType> = remember {
        mutableStateOf(RowType.Row1)
    }

    val configuration: Configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(modifier = Modifier.fillMaxSize()) {
            RowButtonsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                arrangement = arrangement,
                row = row
            )
            RowsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                arrangement = arrangement,
                row = row
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            RowButtonsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                arrangement = arrangement,
                row = row
            )
            RowsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                arrangement = arrangement,
                row = row
            )
        }
    }
}

@Composable
fun RowButtonsContainer(
    modifier: Modifier,
    alignment: MutableState<Alignment.Vertical>,
    arrangement: MutableState<Arrangement.HorizontalOrVertical>,
    row: MutableState<RowType>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .verticalScroll(state = rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            text = "Rows:",
            color = MaterialTheme.colorScheme.primary
        )
        LazyRow(horizontalArrangement = Arrangement.Center) {
            item {
                RowButton(row = row, type = RowType.Row1)
                RowButton(row = row, type = RowType.Row2)
            }
        }

        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            text = "Alignment:",
            color = MaterialTheme.colorScheme.primary
        )
        LazyRow(horizontalArrangement = Arrangement.Center) {
            item {
                AlignmentRowButton(alignment = alignment, vertical = Alignment.Top, name = "Top")
                AlignmentRowButton(alignment = alignment, vertical = Alignment.CenterVertically, name = "CenterVertically")
                AlignmentRowButton(alignment = alignment, vertical = Alignment.Bottom, name = "Bottom")
            }
        }

        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            text = "Arrangement:",
            color = MaterialTheme.colorScheme.primary
        )
        LazyRow(horizontalArrangement = Arrangement.Center) {
            item {
                ArrangementButton(arrangement = arrangement, Arrangement.Center)
                ArrangementButton(arrangement = arrangement, Arrangement.SpaceEvenly)
                ArrangementButton(arrangement = arrangement, Arrangement.SpaceAround)
                ArrangementButton(arrangement = arrangement, Arrangement.SpaceBetween)
                ArrangementButton(arrangement = arrangement, Arrangement.spacedBy(12.dp))
            }
        }
    }
}

@Composable
fun RowsContainer(
    modifier: Modifier,
    alignment: MutableState<Alignment.Vertical>,
    arrangement: MutableState<Arrangement.HorizontalOrVertical>,
    row: MutableState<RowType>
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
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