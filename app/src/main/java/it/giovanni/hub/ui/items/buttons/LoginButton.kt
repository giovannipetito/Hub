package it.giovanni.hub.ui.items.buttons

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.ui.theme.MyShapes
import it.giovanni.hub.utils.Globals.getTransitionColor
import kotlinx.coroutines.delay

@Composable
fun LoginButton(
    text: String = "Log in",
    loadingText: String = "Logging in...",
    validated: Boolean = false,
    onClicked: () -> Unit
) {
    var clicked by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        onClick = {
            clicked = !clicked
        },
        shape = MyShapes.medium,
        border = BorderStroke(width = 1.dp, color = getTransitionColor()),
        // color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 6.dp)
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
                modifier = Modifier.weight(1f),
                painter = painterResource(id = R.drawable.ico_audioslave),
                contentDescription = "Login Button Icon",
                tint = Color.Unspecified
            )
            if (validated) {
                Text(
                    modifier = Modifier.weight(2f),
                    text = if (clicked) loadingText else text,
                    textAlign = TextAlign.Start
                )
                if (clicked) {
                    /*
                    CircularProgressIndicator(
                        modifier = Modifier
                        .weight(1f)
                        .height(16.dp)
                        .width(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    */
                    LaunchedEffect(key1 = "Login Button") {
                        delay(2000)
                        onClicked()
                    }
                }
            } else {
                Text(
                    modifier = Modifier.weight(2f),
                    text = text,
                    textAlign = TextAlign.Start,
                    color = if (isSystemInDarkTheme()) {
                        Color.White.copy(alpha = 0.5f)
                    } else {
                        Color.Black.copy(alpha = 0.5f)
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginButtonPreview() {
    LoginButton(
        text = "Log in",
        loadingText = "Logging in",
        onClicked = {}
    )
}