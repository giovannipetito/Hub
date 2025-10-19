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
import it.giovanni.hub.ui.items.buttons.AlignmentColumnButton
import it.giovanni.hub.ui.items.buttons.ArrangementButton
import it.giovanni.hub.ui.items.Column1
import it.giovanni.hub.ui.items.Column2
import it.giovanni.hub.ui.items.buttons.ColumnButton
import it.giovanni.hub.utils.ColumnType

@Composable
fun HubColumnsScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.columns),
    topics = listOf("Column", "horizontalAlignment", "verticalArrangement", "ColumnScope")
) {
    var alignment by remember {
        mutableStateOf(Alignment.CenterHorizontally)
    }

    var arrangement by remember {
        mutableStateOf(Arrangement.Center)
    }

    var column by remember {
        mutableStateOf(ColumnType.Column1)
    }

    val configuration: Configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(modifier = Modifier.fillMaxSize()) {
            ColumnButtonsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                onAlignmentChange = { alignment = it },
                arrangement = arrangement,
                onArrangementChange = { arrangement = it },
                column = column,
                onColumnChange = { column = it }
            )
            ColumnsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                arrangement = arrangement,
                column = column
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            ColumnButtonsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                onAlignmentChange = { alignment = it },
                arrangement = arrangement,
                onArrangementChange = { arrangement = it },
                column = column,
                onColumnChange = { column = it }
            )
            ColumnsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                arrangement = arrangement,
                column = column
            )
        }
    }
}

@Composable
fun ColumnButtonsContainer(
    modifier: Modifier,
    alignment: Alignment.Horizontal,
    onAlignmentChange: (Alignment.Horizontal) -> Unit,
    arrangement: Arrangement.HorizontalOrVertical,
    onArrangementChange: (Arrangement.HorizontalOrVertical) -> Unit,
    column: ColumnType,
    onColumnChange: (ColumnType) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .verticalScroll(state = rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            text = "Columns:",
            color = MaterialTheme.colorScheme.primary
        )
        LazyRow(horizontalArrangement = Arrangement.Center) {
            item {
                ColumnButton(currentType = column, type = ColumnType.Column1, onChange = onColumnChange)
                ColumnButton(currentType = column, type = ColumnType.Column2, onChange = onColumnChange)
            }
        }

        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            text = "Alignment:",
            color = MaterialTheme.colorScheme.primary
        )
        LazyRow(horizontalArrangement = Arrangement.Center) {
            item {
                AlignmentColumnButton(alignment = alignment, horizontal = Alignment.Start, name = "Start", onChange = onAlignmentChange)
                AlignmentColumnButton(alignment = alignment, horizontal = Alignment.CenterHorizontally, name = "CenterHorizontally", onChange = onAlignmentChange)
                AlignmentColumnButton(alignment = alignment, horizontal = Alignment.End, name = "End", onChange = onAlignmentChange)
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
fun ColumnsContainer(
    modifier: Modifier,
    alignment: Alignment.Horizontal,
    arrangement: Arrangement.HorizontalOrVertical,
    column: ColumnType
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        when (column) {
            ColumnType.Column1 -> Column1(alignment = alignment, arrangement = arrangement)
            ColumnType.Column2 -> Column2(alignment = alignment, arrangement = arrangement)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HubColumnsScreenPreview() {
    HubColumnsScreen(navController = rememberNavController())
}