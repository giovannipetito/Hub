package it.giovanni.hub.utils.swipeableaction

import kotlin.math.abs

internal data class SwipeActionMeta(
    val value: SwipeAction,
    val isOnRightSide: Boolean
)

internal data class ActionFinder(
    val leftActions: List<SwipeAction>,
    val rightActions: List<SwipeAction>
) {
    fun actionAt(offset: Float, totalWidth: Int): SwipeActionMeta? {
        if (offset == 0f) {
            return null
        }

        val isOnRightSide = offset < 0f
        val actions = if (isOnRightSide) rightActions else leftActions

        val actionAtOffset = actions.actionAt(
            offset = abs(offset).coerceAtMost(totalWidth.toFloat()),
            totalWidth = totalWidth
        )
        return actionAtOffset?.let {
            SwipeActionMeta(
                value = actionAtOffset,
                isOnRightSide = isOnRightSide
            )
        }
    }

    private fun List<SwipeAction>.actionAt(offset: Float, totalWidth: Int): SwipeAction? {
        if (isEmpty()) {
            return null
        }

        val totalWeights = this.sumOf { it.weight }
        var offsetSoFar = 0.0

        for (swipeAction in this) {
            val actionWidth = (swipeAction.weight / totalWeights) * totalWidth
            val actionEndX = offsetSoFar + actionWidth

            if (offset <= actionEndX) {
                return swipeAction
            }
            offsetSoFar += actionEndX
        }

        error("Couldn't find any swipe action. Width=$totalWidth, offset=$offset, actions=$this")
    }
}