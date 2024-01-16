package it.giovanni.hub.utils.swipeactions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Represents an action that can be shown in [SwipeActionsBox].
 *
 * @param background Color used as the background of [SwipeActionsBox] while this action is
 * visible. If this action is swiped, its background color is also used for drawing a ripple over
 * the content for providing a visual feedback to the user.
 *
 * @param weight The proportional width to give to this element, as related to the total of all
 * weighted siblings. [SwipeActionsBox] will divide its horizontal space and distribute it
 * to actions according to their weight.
 */
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

data class SwipedAction(
    val swipeActions: List<SwipeAction>,
    val isOnRightSide: Boolean
)

data class SwipeActionFinder(
    val leftActions: List<SwipeAction>,
    val rightActions: List<SwipeAction>
) {
    fun actionAt(offset: Float): SwipedAction? {
        if (offset == 0f) {
            return null
        }

        val isOnRightSide = offset < 0f

        return SwipedAction(
            swipeActions = if (isOnRightSide) rightActions else leftActions,
            isOnRightSide = isOnRightSide
        )
    }
}