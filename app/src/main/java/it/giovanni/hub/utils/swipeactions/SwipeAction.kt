package it.giovanni.hub.utils.swipeactions

import androidx.compose.runtime.Composable

/**
 * Represents an action that can be shown in [SwipeActionsBox].
 */
class SwipeAction(
    val onSwipe: (String) -> Unit,
    val icon: @Composable () -> Unit,
)

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