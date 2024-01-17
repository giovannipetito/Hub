package it.giovanni.hub.utils.swipeactions

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * A composable that can be swiped left or right for revealing actions.
 *
 * @param swipeThreshold Minimum drag distance before any [SwipeAction] is activated and can be swiped.
 *
 * Color.DarkGray is drawn behind the content until [swipeThreshold] is reached. When the threshold
 * is passed, this color is covered by the currently visible [SwipeAction]'s background.
 */
@Composable
fun SwipeActionsBox(
    state: SwipeActionsState = rememberSwipeActionsState(),
    leftActions: List<SwipeAction> = emptyList(),
    rightActions: List<SwipeAction> = emptyList(),
    swipeThreshold: Dp = 96.dp,
    content: @Composable BoxScope.() -> Unit
) = BoxWithConstraints {
    state.also { state ->
        state.layoutWidth = constraints.maxWidth
        state.swipeThresholdPx = LocalDensity.current.run { swipeThreshold.toPx() }
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        state.swipeActionFinder = remember(leftActions, rightActions, isRtl) {
            SwipeActionFinder(
                leftActions = if (isRtl) rightActions else leftActions,
                rightActions = if (isRtl) leftActions else rightActions,
            )
        }
    }

    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .absoluteOffset { IntOffset(x = state.offset.value.roundToInt(), y = 0) }
            .draggable(
                orientation = Horizontal,
                enabled = !state.isResettingOnRelease,
                onDragStopped = {
                    scope.launch {
                        state.onDragStopped()
                    }
                },
                state = state.draggableState,
            ),
        content = content
    )

    (state.swipeActions ?: state.swipeActionsVisible)?.let { swipedAction ->

        val swipeBackground: Color by animateColorAsState(
            when {
                !state.hasCrossedSwipeThreshold() -> Color.DarkGray
                else -> Color.Transparent
            }, label = "backgroundColor"
        )

        SwipeActionsContent(
            modifier = Modifier.matchParentSize(),
            swipeActions = swipedAction,
            offset = state.offset.value,
            swipeBackground = swipeBackground,
            content = {
                if (state.hasCrossedSwipeThreshold()) {
                    for (i in 0..<swipedAction.swipeActions.size) {
                        swipedAction.swipeActions[i].icon()
                    }
                }
            }
        )
    }
}

@Composable
private fun SwipeActionsContent(
    modifier: Modifier = Modifier,
    swipeActions: SwipeActions,
    offset: Float,
    swipeBackground: Color,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(width = placeable.width, height = placeable.height) {
                    // Align icon with the left/right edge of the content being swiped.
                    val iconOffset =
                        if (swipeActions.isOnRightSide) constraints.maxWidth + offset
                        else offset - placeable.width
                    placeable.placeRelative(x = iconOffset.roundToInt(), y = 0)
                }
            }
            .background(color = swipeBackground),
        horizontalArrangement = if (swipeActions.isOnRightSide) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}