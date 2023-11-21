package it.giovanni.hub.ui.items.buttons

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.ui.theme.MyShapes
import kotlinx.coroutines.delay

@Composable
fun GoogleButton(
    text: String = "Sign Up with Google",
    loadingText: String = "Creating Account...",
    icon: Painter = painterResource(id = R.drawable.ic_google_logo),
    shape: Shape = MyShapes.medium,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    validated: Boolean = false,
    onClicked: () -> Unit
) {
    val clicked = mutableStateOf(false)

    Surface(
        onClick = {
            clicked.value = !clicked.value
        },
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = "Google Button",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (validated) {
                Text(text = if (clicked.value) loadingText else text)
                if (clicked.value) {
                    Spacer(modifier = Modifier.width(16.dp))
                    /*
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(16.dp)
                            .width(16.dp),
                        strokeWidth = 2.dp,
                        color = progressIndicatorColor
                    )
                    */
                    LaunchedEffect(key1 = "") {
                        delay(2000)
                        onClicked()
                    }
                }
            } else {
                Text(
                    text = text,
                    color = if (isSystemInDarkTheme()) {
                        Color.White.copy(alpha = 0.5f)
                    }
                    else {
                        Color.Black.copy(alpha = 0.5f)
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GoogleButtonPreview() {
    GoogleButton(
        text = "Sign Up with Google",
        loadingText = "Creating Account",
        onClicked = {}
    )
}