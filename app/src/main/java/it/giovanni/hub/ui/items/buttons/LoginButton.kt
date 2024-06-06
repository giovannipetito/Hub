package it.giovanni.hub.ui.items.buttons

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
    loadingText: String = "Logging in",
    validated: Boolean = false,
    onClick: () -> Unit
) {
    var clicked by remember {
        mutableStateOf(false)
    }

    OutlinedButton(
        modifier = Modifier
            .height(height = 56.dp)
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        enabled = validated,
        onClick = {
            clicked = !clicked
        },
        shape = MyShapes.medium,
        border = BorderStroke(width = 1.dp, color = getTransitionColor()),
        contentPadding = PaddingValues(start = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                modifier = Modifier.size(size = 48.dp),
                painter = painterResource(id = R.drawable.ico_audioslave),
                contentDescription = "Login Button Icon",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(width = 6.dp))
            if (validated) {
                Text(
                    modifier = Modifier.weight(weight = 1f),
                    text = if (clicked) loadingText else text,
                    textAlign = TextAlign.Start,
                    color = Color.White
                )
                if (clicked) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(size = 36.dp),
                        color = Color.Magenta,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 2.dp
                    )
                    LaunchedEffect(key1 = "Login Button") {
                        delay(2000)
                        onClick()
                    }
                }
            } else {
                Text(
                    modifier = Modifier.weight(1f),
                    text = text,
                    textAlign = TextAlign.Start,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = false)
fun LoginButtonPreview() {
    LoginButton(
        text = "Log in",
        loadingText = "Logging in",
        onClick = {}
    )
}