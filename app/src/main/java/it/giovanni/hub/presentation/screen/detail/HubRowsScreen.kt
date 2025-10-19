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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var alignment by remember {
        mutableStateOf(Alignment.CenterVertically)
    }

    var arrangement by remember {
        mutableStateOf(Arrangement.Center)
    }

    var row by remember { mutableStateOf(RowType.Row1) }

    val configuration: Configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(modifier = Modifier.fillMaxSize()) {
            RowButtonsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                onAlignmentChange = { alignment = it },
                arrangement = arrangement,
                onArrangementChange = { arrangement = it },
                row = row,
                onRowChange = { row = it }
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
                onAlignmentChange = { alignment = it },
                arrangement = arrangement,
                onArrangementChange = { arrangement = it },
                row = row,
                onRowChange = { row = it }
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
    alignment: Alignment.Vertical,
    onAlignmentChange: (Alignment.Vertical) -> Unit,
    arrangement: Arrangement.HorizontalOrVertical,
    onArrangementChange: (Arrangement.HorizontalOrVertical) -> Unit,
    row: RowType,
    onRowChange: (RowType) -> Unit
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
                RowButton(currentType = row, type = RowType.Row1, onChange = onRowChange)
                RowButton(currentType = row, type = RowType.Row2, onChange = onRowChange)
            }
        }

        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            text = "Alignment:",
            color = MaterialTheme.colorScheme.primary
        )
        LazyRow(horizontalArrangement = Arrangement.Center) {
            item {
                AlignmentRowButton(alignment = alignment, vertical = Alignment.Top, name = "Top", onChange = onAlignmentChange)
                AlignmentRowButton(alignment = alignment, vertical = Alignment.CenterVertically, name = "CenterVertically", onChange = onAlignmentChange)
                AlignmentRowButton(alignment = alignment, vertical = Alignment.Bottom, name = "Bottom", onChange = onAlignmentChange)
            }
        }

        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            text = "Arrangement:",
            color = MaterialTheme.colorScheme.primary
        )
        LazyRow(horizontalArrangement = Arrangement.Center) {
            item {
                ArrangementButton(arrangement = arrangement, horizontalOrVertical = Arrangement.Center, onChange = onArrangementChange)
                ArrangementButton(arrangement = arrangement, horizontalOrVertical = Arrangement.SpaceEvenly, onChange = onArrangementChange)
                ArrangementButton(arrangement = arrangement, horizontalOrVertical = Arrangement.SpaceAround, onChange = onArrangementChange)
                ArrangementButton(arrangement = arrangement, horizontalOrVertical = Arrangement.SpaceBetween, onChange = onArrangementChange)
                ArrangementButton(arrangement = arrangement, horizontalOrVertical = Arrangement.spacedBy(12.dp), onChange = onArrangementChange)
            }
        }
    }
}

@Composable
fun RowsContainer(
    modifier: Modifier,
    alignment: Alignment.Vertical,
    arrangement: Arrangement.HorizontalOrVertical,
    row: RowType
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        when (row) {
            RowType.Row1 -> Row1(alignment = alignment, arrangement = arrangement)
            RowType.Row2 -> Row2(alignment = alignment, arrangement = arrangement)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubRowsScreenPreview() {
    HubRowsScreen(navController = rememberNavController())
}