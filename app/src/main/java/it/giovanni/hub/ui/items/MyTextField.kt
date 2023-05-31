package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

val rainbowColors: List<Color> = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldStateful(label: String, text: MutableState<String>) {

    TextField(
        value = text.value,
        label = { Text(text = label) },
        placeholder = { Text(text = "Type here...") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.padding(20.dp),
        onValueChange = { input ->
            text.value = input
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldStateless(label: String, text: String, onTextChange: (String) -> Unit) {

    TextField(
        value = text,
        label = { Text(text = label) },
        placeholder = { Text(text = "Type here...") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.padding(20.dp),
        onValueChange = onTextChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldEmail(text: MutableState<TextFieldValue>) {

    val brush = remember { Brush.linearGradient(colors = rainbowColors) }

    OutlinedTextField(
        value = text.value,
        label = { Text(text = "Email address") },
        placeholder = { Text(text = "Enter your e-mail") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "emailIcon") },
        modifier = Modifier.padding(20.dp),
        textStyle = TextStyle(brush = brush),
        onValueChange = { input ->
            text.value = input
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldPassword(text: MutableState<TextFieldValue>) {

    val brush = remember { Brush.linearGradient(colors = rainbowColors) }

    OutlinedTextField(
        value = text.value,
        label = { Text(text = "Password") },
        placeholder = { Text(text = "Enter your password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "lockIcon") },
        modifier = Modifier.padding(20.dp),
        textStyle = TextStyle(brush = brush),
        onValueChange = { input ->
            text.value = input
        }
    )
}