package it.giovanni.hub.ui.items

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.giovanni.hub.R

@Composable
fun Text1() {
    Text(
        text = "Hello, World!",
        modifier = Modifier
            .width(200.dp)
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        color = Color.White,
        fontSize = 20.sp,
        textAlign = TextAlign.End
    )
}

@Composable
fun Text2(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(12.dp),
        color = Color.White,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Start
    )
}

@Composable
fun Text3() {
    Text(
        text = stringResource(id = R.string.hello_world).repeat(20),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CapitalText(color: Color) {
    Text(
        buildAnnotatedString {
            withStyle(
                style = ParagraphStyle(textAlign = TextAlign.Center)
            ) {
                withStyle(
                    style = SpanStyle(
                        color = color,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("G")
                }
                append("I")
                append("O")
                append("V")
                append("A")
                append("N")
                append("N")
                append("I")
            }
        }, modifier = Modifier.width(200.dp)
    )
}

@Composable
fun DisableSelectionText() {
    SelectionContainer {
        Column {
            Text(text = "Selectable Text")
            DisableSelection {
                Text(text = "Non-Selectable Text")
            }
            Text(text = "Selectable Text")
        }
    }
}

@Composable
fun ScriptText(
    normalText: String,
    normalFontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize,
    scriptText: String,
    scriptTextColor: Color = MaterialTheme.colorScheme.primary,
    scriptTextFontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize,
    scriptTextFontWeight: FontWeight = FontWeight.Normal,
    scriptTextBaselineShift: BaselineShift = BaselineShift.Superscript
) {
    Text(buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = normalFontSize
            )
        ) {
            append(normalText)
        }
        withStyle(
            style = SpanStyle(
                color = scriptTextColor,
                fontSize = scriptTextFontSize,
                fontWeight = scriptTextFontWeight,
                baselineShift = scriptTextBaselineShift
            )
        ) {
            append(scriptText)
        }
    })
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TextVerticalAnimation(seconds: Any, slideOutVertically: Boolean) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = seconds,
            transitionSpec = {
                addVerticalAnimation(
                    duration = 800,
                    slideOutVertically = slideOutVertically
                ).using(SizeTransform(clip = false))
            }, label = "Animated Content"
        ) { targetCount ->
            Text(
                text = "$targetCount",
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TextHorizontalAnimation(seconds: Any, slideOutHorizontally: Boolean) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = seconds,
            transitionSpec = {
                addHorizontalAnimation(
                    duration = 800,
                    slideOutHorizontally = slideOutHorizontally
                ).using(SizeTransform(clip = false))
            }, label = "Animated Content"
        ) { targetCount ->
            Text(
                text = "$targetCount",
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                textAlign = TextAlign.Center
            )
        }
    }
}

@ExperimentalAnimationApi
fun addVerticalAnimation(duration: Int, slideOutVertically: Boolean): ContentTransform {
    return (slideInVertically(animationSpec = tween(durationMillis = duration)) {
            height -> height } + fadeIn(animationSpec = tween(durationMillis = duration))
            ).togetherWith(
            androidx.compose.animation.slideOutVertically(
                animationSpec = tween(
                    durationMillis = duration
                )
            ) { height -> if (slideOutVertically) -height else height } +
                fadeOut(animationSpec = tween(durationMillis = duration)))
}

@ExperimentalAnimationApi
fun addHorizontalAnimation(duration: Int, slideOutHorizontally: Boolean): ContentTransform {
    return (slideInHorizontally(animationSpec = tween(durationMillis = duration)) {
            height -> height } + fadeIn(animationSpec = tween(durationMillis = duration))
            ).togetherWith(
            androidx.compose.animation.slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = duration
                )
            ) { height -> if (slideOutHorizontally) -height else height } +
                fadeOut(animationSpec = tween(durationMillis = duration)))
}

@Composable
fun DescriptionText(description: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = description,
        color = MaterialTheme.colorScheme.primary
    )
}

@Preview(showBackground = true)
@Composable
fun Text1Preview() {
    Text1()
}

@Preview(showBackground = true)
@Composable
fun Text2Preview() {
    Text2("Giovanni", modifier = Modifier)
}

@Preview(showBackground = true)
@Composable
fun Text3Preview() {
    Text3()
}

@Preview(showBackground = true)
@Composable
fun CapitalTextPreview() {
    CapitalText(color = MaterialTheme.colorScheme.primary)
}

@Preview(showBackground = true)
@Composable
fun DisableSelectionTextPreview() {
    DisableSelectionText()
}

@Preview(showBackground = true)
@Composable
fun ScriptTextPreview() {
    ScriptText(
        normalText = "Giovanni",
        normalFontSize = MaterialTheme.typography.titleMedium.fontSize,
        scriptText = "Petito",
        scriptTextFontSize = MaterialTheme.typography.titleMedium.fontSize,
        scriptTextFontWeight = FontWeight.Normal,
        scriptTextBaselineShift = BaselineShift.Superscript
    )
}

@Preview(showBackground = true)
@Composable
fun TextVerticalAnimationPreview() {
    TextVerticalAnimation(seconds = 100, slideOutVertically = true)
}

@Preview(showBackground = true)
@Composable
fun TextHorizontalAnimationPreview() {
    TextHorizontalAnimation(seconds = 100, slideOutHorizontally = true)
}

@Preview(showBackground = true)
@Composable
fun DescriptionTextPreview() {
    DescriptionText(description = "Hello, World!")
}