package it.giovanni.hub.utils.swipeactions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import it.giovanni.hub.utils.Constants.SWIPE_DURATION
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun rememberSwipeActionsState(): SwipeActionsState {
    return remember { SwipeActionsState() }
}

/**
 * The state of a [SwipeActionsBox].
 */
@Stable
class SwipeActionsState {

    /**
     * The current position (in pixels) of a [SwipeActionsBox].
     */
    val offset: State<Float> get() = offsetState

    private var offsetState = mutableFloatStateOf(0f)

    /**
     * Whether [SwipeActionsBox] is currently animating to reset its offset after it was swiped.
     */
    val isResettingOnRelease: Boolean by derivedStateOf {
        swipeActions != null
    }

    var layoutWidth: Int by mutableIntStateOf(0)

    var swipeThresholdPx: Float by mutableFloatStateOf(0f)

    var swipeActionFinder: SwipeActionFinder by mutableStateOf(
        SwipeActionFinder(leftActions = emptyList(), rightActions = emptyList())
    )

    val swipeActionsVisible: SwipeActions? by derivedStateOf {
        swipeActionFinder.actionAt(offsetState.floatValue)
    }

    var swipeActions: SwipeActions? by mutableStateOf(null)

    val draggableState = DraggableState { delta ->
        val targetOffset = offsetState.floatValue + delta

        val isAllowed = isResettingOnRelease
                || targetOffset == 0f
                || (targetOffset > 0f && swipeActionFinder.leftActions.isNotEmpty())
                || (targetOffset < 0f && swipeActionFinder.rightActions.isNotEmpty())

        offsetState.floatValue += if (isAllowed) delta else delta / 10
    }

    fun hasCrossedSwipeThreshold(): Boolean {
        return abs(offsetState.floatValue) > swipeThresholdPx
    }

    suspend fun onDragStopped() = coroutineScope {
        launch {
            if (hasCrossedSwipeThreshold()) {
                swipeActionsVisible?.let { action ->
                    swipeActions = action
                    swipeAnimation()

                    if (action.swipeActions.size == 1)
                        action.swipeActions[0].onSwipe("Swipe")
                }
            }
        }
        launch {
            if (hasCrossedSwipeThreshold()) {
                val swippedWidth = (layoutWidth.toFloat() * 2)/3
                draggableState.drag(MutatePriority.PreventUserInput) {
                    Animatable(offsetState.floatValue).animateTo(
                        targetValue = if (swipeActionsVisible?.isOnRightSide!!) - swippedWidth else swippedWidth,
                        animationSpec = tween(durationMillis = SWIPE_DURATION),
                    ) {
                        dragBy(value - offsetState.floatValue)
                    }
                }
            } else {
                draggableState.drag(MutatePriority.PreventUserInput) {
                    Animatable(offsetState.floatValue).animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = SWIPE_DURATION),
                    ) {
                        dragBy(value - offsetState.floatValue)
                    }
                }
            }
            swipeActions = null
        }
    }

    private suspend fun swipeAnimation() {
        coroutineScope {
            launch {
                Animatable(initialValue = 0f).animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = SWIPE_DURATION,
                        delayMillis = SWIPE_DURATION / 2
                    )
                )
            }
        }
    }
}