package it.giovanni.hub.presentation.screen.detail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.ui.items.cards.ExpandableCard
import it.giovanni.hub.ui.items.CircularIndicator
import it.giovanni.hub.ui.items.SelectableItem
import it.giovanni.hub.utils.Constants
import it.giovanni.hub.utils.Globals.isScrolled

@Composable
fun UIScreen(navController: NavController) {

    var selected1 by remember { mutableStateOf(false) }
    var selected2 by remember { mutableStateOf(false) }

    val lazyListState: LazyListState = rememberLazyListState()

    val checked = remember { mutableStateOf(true) }
    val animatedBlur = animateDpAsState(targetValue = if (checked.value) 10.dp else 0.dp, label = "animatedBlur")

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            state = lazyListState
        ) {
            item {
                ExpandableCard(
                    modifier = Modifier.padding(all = 12.dp),
                    title = "Expandable Card"
                )

                var value by remember {
                    mutableIntStateOf(0)
                }
                CircularIndicator(
                    indicatorValue = value
                )
                TextField(
                    value = value.toString(),
                    onValueChange = { input ->
                        value = if (input.isNotEmpty()) {
                            input.toInt()
                        } else {
                            0
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(12.dp))

                SelectableItem(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    selected = selected1,
                    title = "Selectable Item 1",
                    onClick = {
                        selected1 = !selected1
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SelectableItem(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    selected = selected2,
                    title = "Selectable Item 2",
                    subtitle = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.",
                    onClick = {
                        selected2 = !selected2
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = Modifier
                        .blur(radius = animatedBlur.value, edgeTreatment = BlurredEdgeTreatment.Unbounded),
                    text = "Blur Effect",
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.displayMedium.fontSize,
                        fontWeight = FontWeight.Normal
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Switch(checked = checked.value, onCheckedChange = {checked.value = !checked.value})

                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                SubList()
            }
        }

        Text(
            text = if (lazyListState.isScrolled) "Scrolling..." else "Inactive",
            style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SubList() {
    val icons = Constants.icons + Constants.icons
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(count = icons.size) {index ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.Blue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icons[index],
                    contentDescription = "Icon",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UIScreenPreview() {
    UIScreen(navController = rememberNavController())
}