package it.giovanni.hub.ui.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.giovanni.hub.R

@Composable
fun Text1() {
    Text(
        text = "Hello, World!",
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.primary)
            .width(200.dp),
        color = Color.White,
        fontSize = 20.sp
    )
}

@Composable
fun Text2() {
    Text(
        text = stringResource(id = R.string.hello_world),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
            .width(200.dp),
        color = Color.White,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.End
    )
}

@Composable
fun Text3(color: Color) {
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
                    append("A")
                }
                append("B")
                append("C")
                append("D")
            }
        }, modifier = Modifier.width(200.dp)
    )
}

@Composable
fun Text4() {
    Text(
        text = stringResource(id = R.string.hello_world).repeat(20),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun Text5() {
    SelectionContainer {
        Column {
            Text(text = stringResource(id = R.string.hello_world))
            DisableSelection {
                Text(text = stringResource(id = R.string.hello_world))
            }
            Text(text = stringResource(id = R.string.hello_world))
        }
    }
}

@Composable
fun ScriptText(
    normalText: String,
    normalFontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize,
    scriptText: String,
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
                fontSize = scriptTextFontSize,
                fontWeight = scriptTextFontWeight,
                baselineShift = scriptTextBaselineShift
            )
        ) {
            append(scriptText)
        }
    })
}