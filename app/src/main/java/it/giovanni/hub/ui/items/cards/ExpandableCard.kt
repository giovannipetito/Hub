package it.giovanni.hub.ui.items.cards

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.giovanni.hub.R
import it.giovanni.hub.ui.theme.MyShapes
import it.giovanni.hub.utils.Constants

@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    title: String,
) {
    val expandedState = remember { mutableStateOf(false) }
    val rotationState = animateFloatAsState(
        targetValue = if (expandedState.value) 180f else 0f,
        label = "Rotation State"
    )
    Card(modifier = modifier
        .fillMaxWidth()
        .animateContentSize(
            animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
        ), shape = MyShapes.medium,
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.weight(6f),
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .alpha(.3f)
                        .weight(1f)
                        .rotate(rotationState.value),
                    onClick = {
                        expandedState.value = expandedState.value.not()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ico_audioslave),
                        contentDescription = "Drop-down Arrow Icon"
                    )
                }
            }
            if (expandedState.value) {
                Text(text = Constants.LOREM_IPSUM_LONG_TEXT,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 26,
                    overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableCardPreview() {
    ExpandableCard(modifier = Modifier, title = "Expandable Card")
}