package it.giovanni.hub.utils.swipeableaction

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
import it.giovanni.hub.utils.Constants.SWIPEABLE_ANIMATION_DURATION
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun rememberSwipeableActionsState(): SwipeableActionsState {
    return remember { SwipeableActionsState() }
}

/**
 * The state of a [SwipeableActionsBox].
 */
@Stable
class SwipeableActionsState internal constructor() {
    /**
     * The current position (in pixels) of a [SwipeableActionsBox].
     */
    val offset: State<Float> get() = offsetState
    private var offsetState = mutableFloatStateOf(0f)

    /**
     * Whether [SwipeableActionsBox] is currently animating to reset its offset after it was swiped.
     */
    val isResettingOnRelease: Boolean by derivedStateOf {
        swipedAction != null
    }

    internal var layoutWidth: Int by mutableIntStateOf(0)
    internal var swipeThresholdPx: Float by mutableFloatStateOf(0f)
    private val ripple = SwipeRippleState()

    internal var actions: ActionFinder by mutableStateOf(
        ActionFinder(leftActions = emptyList(), rightActions = emptyList())
    )
    internal val visibleAction: SwipeActionMeta? by derivedStateOf {
        actions.actionAt(offsetState.floatValue, totalWidth = layoutWidth)
    }
    internal var swipedAction: SwipeActionMeta? by mutableStateOf(null)

    internal val draggableState = DraggableState { delta ->
        val targetOffset = offsetState.floatValue + delta

        val isAllowed = isResettingOnRelease
                || targetOffset == 0f
                || (targetOffset > 0f && actions.leftActions.isNotEmpty())
                || (targetOffset < 0f && actions.rightActions.isNotEmpty())

        offsetState.floatValue += if (isAllowed) delta else delta / 10
    }

    internal fun hasCrossedSwipeThreshold(): Boolean {
        return abs(offsetState.floatValue) > swipeThresholdPx
    }

    internal suspend fun handleOnDragStopped() = coroutineScope {
        launch {
            if (hasCrossedSwipeThreshold()) {
                visibleAction?.let { action ->
                    swipedAction = action
                    action.value.onSwipe()
                    ripple.animate(action = action)
                }
            }
        }
        launch {
            if (hasCrossedSwipeThreshold()) {
                val swippedWidth = (layoutWidth.toFloat() * 2)/3
                draggableState.drag(MutatePriority.PreventUserInput) {
                    Animatable(offsetState.floatValue).animateTo(
                        targetValue = if (visibleAction?.isOnRightSide!!) - swippedWidth else swippedWidth,
                        animationSpec = tween(durationMillis = SWIPEABLE_ANIMATION_DURATION),
                    ) {
                        dragBy(value - offsetState.floatValue)
                    }
                }
            } else {
                draggableState.drag(MutatePriority.PreventUserInput) {
                    Animatable(offsetState.floatValue).animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = SWIPEABLE_ANIMATION_DURATION),
                    ) {
                        dragBy(value - offsetState.floatValue)
                    }
                }
            }
            swipedAction = null
        }
    }
}