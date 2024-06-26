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
    val alignment: MutableState<Alignment.Horizontal> = remember {
        mutableStateOf(Alignment.CenterHorizontally)
    }

    val arrangement: MutableState<Arrangement.HorizontalOrVertical> = remember {
        mutableStateOf(Arrangement.Center)
    }

    val column: MutableState<ColumnType> = remember {
        mutableStateOf(ColumnType.Column1)
    }

    val configuration: Configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(modifier = Modifier.fillMaxSize()) {
            ColumnButtonsContainer(
                modifier = Modifier.weight(1f),
                alignment = alignment,
                arrangement = arrangement,
                column = column
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
                arrangement = arrangement,
                column = column
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
    alignment: MutableState<Alignment.Horizontal>,
    arrangement: MutableState<Arrangement.HorizontalOrVertical>,
    column: MutableState<ColumnType>
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
                ColumnButton(column = column, type = ColumnType.Column1)
                ColumnButton(column = column, type = ColumnType.Column2)
            }
        }

        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            text = "Alignment:",
            color = MaterialTheme.colorScheme.primary
        )
        LazyRow(horizontalArrangement = Arrangement.Center) {
            item {
                AlignmentColumnButton(alignment = alignment, horizontal = Alignment.Start, name = "Start")
                AlignmentColumnButton(alignment = alignment, horizontal = Alignment.CenterHorizontally, name = "CenterHorizontally")
                AlignmentColumnButton(alignment = alignment, horizontal = Alignment.End, name = "End")
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
fun ColumnsContainer(
    modifier: Modifier,
    alignment: MutableState<Alignment.Horizontal>,
    arrangement: MutableState<Arrangement.HorizontalOrVertical>,
    column: MutableState<ColumnType>
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
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