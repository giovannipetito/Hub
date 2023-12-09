package it.giovanni.hub.ui.items

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R
import it.giovanni.hub.utils.Globals.getBrushLoginColors

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
fun OutlinedTextFieldEmail(email: MutableState<TextFieldValue>) {

    val brushLoginColors = getBrushLoginColors()
    val brush = remember { Brush.linearGradient(colors = brushLoginColors) }

    OutlinedTextField(
        modifier = Modifier.padding(start = 40.dp, top = 20.dp, end = 0.dp, bottom = 20.dp),
        value = email.value,
        label = { Text(text = "Email") },
        placeholder = { Text(text = "Enter your email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
        textStyle = TextStyle(brush = brush),
        maxLines = 1,
        onValueChange = { input ->
            email.value = input
        }
    )
}

@Composable
fun OutlinedTextFieldPassword(password: MutableState<TextFieldValue>) {

    val passwordVisibility = remember { mutableStateOf(false) }
    val icon = if (passwordVisibility.value) painterResource(id = R.drawable.ico_show_password) else painterResource(id = R.drawable.ico_hide_password)

    val brushLoginColors = getBrushLoginColors()
    val brush = remember { Brush.linearGradient(colors = brushLoginColors) }

    OutlinedTextField(
        modifier = Modifier.padding(20.dp),
        value = password.value,
        label = { Text(text = "Password") },
        placeholder = { Text(text = "Enter your password") },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon") },
        trailingIcon = { IconButton(onClick = {
            passwordVisibility.value = passwordVisibility.value.not()
        }) {
            Icon(painter = icon, contentDescription = "Visibility Icon")
        }},
        textStyle = TextStyle(brush = brush),
        maxLines = 1,
        onValueChange = { input ->
            password.value = input
        }
    )
}