package it.giovanni.hub.ui.items.cards

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun SelectableCard(
    modifier: Modifier = Modifier,
    selected: Boolean,
    title: String,
    titleColor: Color = setSelectableCardColor(selected = selected, color = MaterialTheme.colorScheme.primary),
    titleSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize,
    titleWeight: FontWeight = FontWeight.Medium,
    subtitle: String? = null,
    subtitleColor: Color = setSelectableCardColor(selected = selected, color = MaterialTheme.colorScheme.onSurface),
    borderWidth: Dp = 1.dp,
    borderColor: Color = setSelectableCardColor(selected = selected, color = MaterialTheme.colorScheme.outline),
    borderShape: Shape = RoundedCornerShape(size = 10.dp),
    icon: Painter = painterResource(id = R.drawable.ico_done),
    iconColor: Color = setSelectableCardColor(selected = selected, color = MaterialTheme.colorScheme.primary),
    onClick: () -> Unit
) {
    val scaleColumn = remember { Animatable(initialValue = 1f) } // It handles the Column animation.
    val scaleIcon = remember { Animatable(initialValue = 1f) } // It handles the Icon animation.

    val clickEnabled: MutableState<Boolean> = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = selected) {
        if (selected) {
            clickEnabled.value = false

            val jobColumn: Job = launch {
                scaleColumn.animateTo(
                    targetValue = 0.9f,
                    animationSpec = tween(
                        durationMillis = 50
                    )
                )
                scaleColumn.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }

            val jobIcon: Job = launch {
                scaleIcon.animateTo(
                    targetValue = 0.3f,
                    animationSpec = tween(
                        durationMillis = 50
                    )
                )
                scaleIcon.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }

            jobColumn.join()
            jobIcon.join()
            clickEnabled.value = true
        }
    }

    Column(
        modifier = modifier
            .scale(scale = scaleColumn.value)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = borderShape
            )
            .clip(shape = borderShape)
            .clickable(enabled = clickEnabled.value) {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(8f),
                text = title,
                style = TextStyle(
                    color = titleColor,
                    fontSize = titleSize,
                    fontWeight = titleWeight
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                modifier = Modifier
                    .weight(2f)
                    .scale(scale = scaleIcon.value),
                onClick = {
                    if (clickEnabled.value) {
                        onClick()
                    }
                }
            ) {
                Icon(
                    painter = icon,
                    contentDescription = "Selectable Card Icon",
                    tint = iconColor
                )
            }
        }
        if (subtitle != null) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                text = subtitle,
                style = TextStyle(
                    color = subtitleColor
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun setSelectableCardColor(selected: Boolean, color: Color): Color {
    return if (selected) color
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
}