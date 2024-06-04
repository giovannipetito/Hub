package it.giovanni.hub.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.utils.Globals.getBrushLoginColors
import it.giovanni.hub.utils.Globals.getTransitionColor

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

@Composable
fun OutlinedTextFieldEmail(modifier: Modifier = Modifier, email: MutableState<TextFieldValue>) {

    val brushLoginColors = getBrushLoginColors()
    val brush = remember { Brush.linearGradient(colors = brushLoginColors) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth().padding(start = 40.dp),
        value = email.value,
        label = { Text(text = "Email", color = Color.White) },
        placeholder = { Text(text = "Enter your email", color = Color.White) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Number
        leadingIcon = { Icon(imageVector = Icons.Default.Email, tint = getTransitionColor(), contentDescription = "Email Icon") },
        textStyle = TextStyle(brush = brush),
        singleLine = true,
        maxLines = 1,
        onValueChange = { input ->
            email.value = input
        }
    )
}

@Composable
fun OutlinedTextFieldPassword(modifier: Modifier = Modifier, password: MutableState<TextFieldValue>) {

    val passwordVisibility = remember { mutableStateOf(false) }
    val icon: Painter =
        if (passwordVisibility.value) painterResource(id = R.drawable.ico_show_password)
        else painterResource(id = R.drawable.ico_hide_password)

    val brushLoginColors = getBrushLoginColors()
    val brush = remember { Brush.linearGradient(colors = brushLoginColors) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth().padding(start = 40.dp),
        value = password.value,
        label = { Text(text = "Password", color = Color.White) },
        placeholder = { Text(text = "Enter your password", color = Color.White) },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // NumberPassword
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, tint = getTransitionColor(), contentDescription = "Lock Icon") },
        trailingIcon = { IconButton(onClick = {
            passwordVisibility.value = passwordVisibility.value.not()
        }) {
            Icon(painter = icon, tint = getTransitionColor(), contentDescription = "Visibility Icon")
        }},
        textStyle = TextStyle(brush = brush),
        singleLine = true,
        maxLines = 1,
        onValueChange = { input ->
            password.value = input
        }
    )
}