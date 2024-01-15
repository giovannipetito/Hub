package it.giovanni.hub.utils.swipeableaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

/**
 * Represents an action that can be shown in [SwipeableActionsBox].
 *
 * @param background Color used as the background of [SwipeableActionsBox] while this action is
 * visible. If this action is swiped, its background color is also used for drawing a ripple over
 * the content for providing a visual feedback to the user.
 *
 * @param weight The proportional width to give to this element, as related to the total of all
 * weighted siblings. [SwipeableActionsBox] will divide its horizontal space and distribute it
 * to actions according to their weight.
 */
fun SwipeAction(
    // onSwipe: () -> Unit,
    background: Color,
    weight: Double = 1.0,
    icon: Painter
): SwipeAction {
    return SwipeAction(
        // onSwipe = onSwipe,
        background = background,
        weight = weight,
        icon = {
            Image(
                modifier = Modifier.padding(16.dp),
                painter = icon,
                contentDescription = null
            )
        }
    )
}

class SwipeAction(
    // val onSwipe: () -> Unit,
    val background: Color,
    val weight: Double = 1.0,
    val icon: @Composable () -> Unit,
) {
    init {
        require(weight > 0.0) { "invalid weight $weight; must be greater than zero." }
    }
}

internal data class SwipedAction(
    val swipeActions: List<SwipeAction>,
    val isOnRightSide: Boolean
)