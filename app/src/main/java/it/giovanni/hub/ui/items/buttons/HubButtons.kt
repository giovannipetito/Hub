package it.giovanni.hub.ui.items.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.utils.ColumnType
import it.giovanni.hub.utils.RowType

@Composable
fun HubButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
fun HubFilledButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = "Filled Button")
    }
}

@Composable
fun HubFilledTonalButton(onClick: () -> Unit) {
    FilledTonalButton(onClick = { onClick() }) {
        Text(text = "Filled Tonal Button")
    }
}

@Composable
fun HubOutlinedButton(onClick: () -> Unit) {
    OutlinedButton(onClick = { onClick() }) {
        Text(text = "Outlined Button")
    }
}

@Composable
fun HubElevatedButton(onClick: () -> Unit) {
    ElevatedButton(onClick = { onClick() }) {
        Text(text = "Elevated Button")
    }
}

@Composable
fun HubTextButton(onClick: () -> Unit) {
    TextButton(onClick = { onClick() }) {
        Text(text = "Text Button")
    }
}

@Composable
fun MainTextButton(onClick: () -> Unit, id: Int) {
    TextButton(onClick = { onClick() }) {
        Text(
            text = stringResource(id = id),
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
        )
    }
}

@Composable
fun ColumnButton(
    currentType: ColumnType,
    type: ColumnType,
    onChange: (ColumnType) -> Unit
) {
    val selected = currentType == type
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = {
            onChange(type)
        }
    ) {
        Text(if (selected) "✓ ${type.name}" else type.name)
    }
}

@Composable
fun RowButton(
    currentType: RowType,
    type: RowType,
    onChange: (RowType) -> Unit
) {
    val selected = currentType == type
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = {
            onChange(type)
        }
    ) {
        Text(if (selected) "✓ ${type.name}" else type.name)
    }
}

@Composable
fun AlignmentColumnButton(
    alignment: Alignment.Horizontal,
    horizontal: Alignment.Horizontal,
    name: String,
    onChange: (Alignment.Horizontal) -> Unit
) {
    val selected = alignment == horizontal
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = { onChange(horizontal) }
    ) {
        Text(if (selected) "✓ $name" else name)
    }
}

@Composable
fun AlignmentRowButton(
    alignment: Alignment.Vertical,
    vertical: Alignment.Vertical,
    name: String,
    onChange: (Alignment.Vertical) -> Unit
) {
    val selected = alignment == vertical
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = { onChange(vertical) }
    ) {
        Text(if (selected) "✓ $name" else name)
    }
}

@Composable
fun ArrangementButton(
    arrangement: Arrangement.HorizontalOrVertical,
    horizontalOrVertical: Arrangement.HorizontalOrVertical,
    onChange: (Arrangement.HorizontalOrVertical) -> Unit
) {
    val selected = arrangement == horizontalOrVertical
    Button(
        modifier = Modifier.padding(horizontal = 8.dp),
        onClick = { onChange(horizontalOrVertical) }
    ) {
        val arrangementName = horizontalOrVertical.toString()
        if (!arrangementName.contains("spacedAligned")) {
            Text(if (selected) "✓ ${arrangementName.substring(startIndex = 12)}" else arrangementName.substring(startIndex = 12))
            // Oppure: Text(horizontalOrVertical.toString().substringAfter(delimiter = "#"))
        } else {
            Text(
                if (selected) "✓ ${arrangementName.substring(startIndex = 12, endIndex = 33)})"
                else "${arrangementName.substring(startIndex = 12, endIndex = 33)})"
            )
        }
    }
}

@Composable
fun GridButton(grid: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick
    ) {
        Text(text = grid)
    }
}

@Preview(showBackground = true)
@Composable
fun HubButtonPreview() {
    HubButton(modifier = Modifier, text = "Hub Button", onClick = {})
}

@Preview(showBackground = true)
@Composable
fun HubFilledButtonPreview() {
    HubFilledButton(onClick = {})
}

@Preview(showBackground = true)
@Composable
fun HubFilledTonalButtonPreview() {
    HubFilledTonalButton(onClick = {})
}

@Preview(showBackground = true)
@Composable
fun HubOutlinedButtonPreview() {
    HubOutlinedButton(onClick = {})
}

@Preview(showBackground = true)
@Composable
fun HubElevatedButtonPreview() {
    HubElevatedButton(onClick = {})
}

@Preview(showBackground = true)
@Composable
fun HubTextButtonPreview() {
    HubTextButton(onClick = {})
}

@Preview(showBackground = true)
@Composable
fun MainTextButtonPreview() {
    MainTextButton(onClick = {}, id = R.string.app_name)
}

@Preview(showBackground = true)
@Composable
fun ColumnButtonPreview() {
    var currentType by remember { mutableStateOf(ColumnType.Column1) }
    ColumnButton(
        currentType = currentType,
        type = ColumnType.Column1,
        onChange = { currentType = it }
    )
}

@Preview(showBackground = true)
@Composable
fun RowButtonPreview() {
    var currentType by remember { mutableStateOf(RowType.Row1) }
    RowButton(
        currentType = currentType,
        type = RowType.Row1,
        onChange = { currentType = it }
    )
}

@Preview(showBackground = true)
@Composable
fun AlignmentColumnButtonPreview() {
    var alignment by remember { mutableStateOf(Alignment.CenterHorizontally) }
    AlignmentColumnButton(
        alignment = alignment,
        horizontal = Alignment.Start,
        name = "Start",
        onChange = { alignment = it }
    )
}

@Preview(showBackground = true)
@Composable
fun AlignmentRowButtonPreview() {
    var alignment by remember { mutableStateOf(Alignment.CenterVertically) }
    AlignmentRowButton(
        alignment = alignment,
        vertical = Alignment.Top,
        name = "Top",
        onChange = { alignment = it }
    )
}

@Preview(showBackground = true)
@Composable
fun ArrangementButtonPreview() {
    var arrangement by remember { mutableStateOf(Arrangement.Center) }
    ArrangementButton(
        arrangement = arrangement,
        horizontalOrVertical = Arrangement.Center,
        onChange = { arrangement = it }
    )
}

@Preview(showBackground = true)
@Composable
fun GridButtonPreview() {
    GridButton(grid = "Grid 1", onClick = {})
}