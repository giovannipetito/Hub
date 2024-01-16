package it.giovanni.hub.utils.swipeactions

internal data class SwipeActionFinder(
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