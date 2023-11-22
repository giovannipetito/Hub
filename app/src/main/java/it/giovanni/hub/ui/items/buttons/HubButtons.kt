package it.giovanni.hub.ui.items.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.giovanni.hub.utils.ColumnType
import it.giovanni.hub.utils.RowType

@Composable
fun ColumnButton(column: MutableState<ColumnType>, type: ColumnType) {
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = {
            column.value = type
        }
    ) {
        Text(type.name)
    }
}

@Composable
fun RowButton(row: MutableState<RowType>, type: RowType) {
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = {
            row.value = type
        }
    ) {
        Text(type.name)
    }
}

@Composable
fun AlignmentColumnButton(
    alignment: MutableState<Alignment.Horizontal>,
    horizontal: Alignment.Horizontal,
    name: String
) {
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = {
            alignment.value = horizontal
        }
    ) {
        Text(name)
    }
}

@Composable
fun AlignmentRowButton(
    alignment: MutableState<Alignment.Vertical>,
    vertical: Alignment.Vertical,
    name: String
) {
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = {
            alignment.value = vertical
        }
    ) {
        Text(name)
    }
}

@Composable
fun ArrangementButton(
    arrangement: MutableState<Arrangement.HorizontalOrVertical>,
    horizontalOrVertical: Arrangement.HorizontalOrVertical
) {
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = {
            arrangement.value = horizontalOrVertical
        }
    ) {
        val arrangementName = horizontalOrVertical.toString()
        if (!arrangementName.contains("spacedAligned")) {
            Text(arrangementName.substring(startIndex = 12))
            // Oppure: Text(horizontalOrVertical.toString().substringAfter(delimiter = "#"))
        } else {
            Text(arrangementName.substring(startIndex = 12, endIndex = 33) + ")")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ColumnButtonPreview() {
    ColumnButton(
        column = mutableStateOf(ColumnType.Column1),
        type = ColumnType.Column1
    )
}

@Preview(showBackground = true)
@Composable
fun RowButtonPreview() {
    RowButton(
        row = mutableStateOf(RowType.Row1),
        type = RowType.Row1
    )
}

@Preview(showBackground = true)
@Composable
fun AlignmentColumnButtonPreview() {
    AlignmentColumnButton(
        alignment = mutableStateOf(Alignment.CenterHorizontally),
        horizontal = Alignment.Start,
        name = "Start"
    )
}

@Preview(showBackground = true)
@Composable
fun AlignmentRowButtonPreview() {
    AlignmentRowButton(
        alignment = mutableStateOf(Alignment.CenterVertically),
        vertical = Alignment.Top,
        name = "Top"
    )
}

@Preview(showBackground = true)
@Composable
fun ArrangementButtonPreview() {
    ArrangementButton(
        arrangement = mutableStateOf(Arrangement.Center),
        Arrangement.Center
    )
}