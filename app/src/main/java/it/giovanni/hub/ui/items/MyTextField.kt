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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import it.giovanni.hub.R

val rainbowColors: List<Color> = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta)

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

    val brush = remember { Brush.linearGradient(colors = rainbowColors) }

    OutlinedTextField(
        value = email.value,
        label = { Text(text = "Email address") },
        placeholder = { Text(text = "Enter your e-mail") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
        modifier = Modifier.padding(20.dp),
        textStyle = TextStyle(brush = brush),
        onValueChange = { input ->
            email.value = input
        }
    )
}

@Composable
fun OutlinedTextFieldPassword(password: MutableState<TextFieldValue>) {

    val passwordVisibility = remember { mutableStateOf(false) }
    val icon = if (passwordVisibility.value) painterResource(id = R.drawable.ico_show_password) else painterResource(id = R.drawable.ico_hide_password)

    val brush = remember { Brush.linearGradient(colors = rainbowColors) }

    OutlinedTextField(
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
        modifier = Modifier.padding(20.dp),
        textStyle = TextStyle(brush = brush),
        onValueChange = { input ->
            password.value = input
        }
    )
}