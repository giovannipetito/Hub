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
 * is passed, this color is replaced by the currently visible [SwipeAction]'s background.
 */
@Composable
fun SwipeActionsBox(
    state: SwipeActionsState = rememberSwipeActionsState(),
    leftActions: List<SwipeAction> = emptyList(),
    rightActions: List<SwipeAction> = emptyList(),
    swipeThreshold: Dp = 96.dp,
    content: @Composable BoxScope.() -> Unit
) = BoxWithConstraints {
    state.also {
        it.layoutWidth = constraints.maxWidth
        it.swipeThresholdPx = LocalDensity.current.run { swipeThreshold.toPx() }
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        it.actions = remember(leftActions, rightActions, isRtl) {
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
                        state.handleOnDragStopped()
                    }
                },
                state = state.draggableState,
            ),
        content = content
    )

    (state.swipedAction ?: state.swipedActionVisible)?.let { swipedAction ->

        val backgroundColor1: Color by animateColorAsState(
            when {
                // state.swipedAction != null -> state.swipedAction!!.value.background
                !state.hasCrossedSwipeThreshold() -> Color.DarkGray
                // state.swipedActionVisible != null -> state.swipedActionVisible!!.value.background
                else -> Color.Transparent
            }, label = "backgroundColor"
        )

        var backgroundColor2: Color = Color.DarkGray

        for (i in 0..<swipedAction.swipeActions.size) {
            backgroundColor2 = swipedAction.swipeActions[i].swipeBackground
        }

        /*
        for (i in 0..<swipedAction.swipeActions.size) {
            backgroundColor2 = when {
                !state.hasCrossedSwipeThreshold() -> Color.DarkGray
                state.swipedAction != null -> state.swipedAction!!.swipeActions[i].swipeBackground
                state.swipedActionVisible != null -> state.swipedActionVisible!!.swipeActions[i].swipeBackground
                else -> Color.Transparent
            }
        }
        */

        /*
        for (i in 0..<swipedAction.swipeActions.size) {
            if (!state.hasCrossedSwipeThreshold()) {
                backgroundColor2 = Color.DarkGray
                break
            } else if (state.swipedAction != null) {
                backgroundColor2 = state.swipedAction!!.swipeActions[i].swipeBackground
                break
            } else if (state.swipedActionVisible != null) {
                backgroundColor2 = state.swipedActionVisible!!.swipeActions[i].swipeBackground
                break
            } else {
                backgroundColor2 = Color.Transparent
                break
            }
        }
        */

        SwipeActionsContent(
            modifier = Modifier.matchParentSize(),
            swipedAction = swipedAction,
            offset = state.offset.value,
            backgroundColor = backgroundColor1,
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
    swipedAction: SwipedAction,
    offset: Float,
    backgroundColor: Color,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(width = placeable.width, height = placeable.height) {
                    // Align icon with the left/right edge of the content being swiped.
                    val iconOffset =
                        if (swipedAction.isOnRightSide) constraints.maxWidth + offset
                        else offset - placeable.width
                    placeable.placeRelative(x = iconOffset.roundToInt(), y = 0)
                }
            }
            .background(color = backgroundColor),
        horizontalArrangement = if (swipedAction.isOnRightSide) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}