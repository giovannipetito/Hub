package it.giovanni.hub.utils.swipeableaction

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import it.giovanni.hub.utils.Constants.SWIPEABLE_ANIMATION_DURATION
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Stable
internal class SwipeRippleState {

    private var ripple = mutableStateOf<SwipeRipple?>(null)

    suspend fun animate(action: SwipedAction) {

        val drawOnRightSide = action.isOnRightSide

        ripple.value = SwipeRipple(
            rightSide = drawOnRightSide,
            alpha = 0f,
            progress = 0f
        )

        coroutineScope {
            launch {
                Animatable(initialValue = 0f).animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = SWIPEABLE_ANIMATION_DURATION),
                    block = {
                        ripple.value = ripple.value!!.copy(progress = value)
                    }
                )
            }
            launch {
                Animatable(initialValue = 0.25f).animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = SWIPEABLE_ANIMATION_DURATION,
                        delayMillis = SWIPEABLE_ANIMATION_DURATION / 2
                    ),
                    block = {
                        ripple.value = ripple.value!!.copy(alpha = value)
                    }
                )
            }
        }
    }
}

private data class SwipeRipple(
    val rightSide: Boolean,
    val alpha: Float,
    val progress: Float
)