package it.giovanni.hub.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.Constants
import it.giovanni.hub.MainActivity
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.OutlinedTextFieldEmail
import it.giovanni.hub.ui.items.OutlinedTextFieldPassword
import it.giovanni.hub.ui.items.TextFieldStateful
import it.giovanni.hub.ui.items.TextFieldStateless
import it.giovanni.hub.viewmodels.TextFieldsViewModel

@Composable
fun TextFieldsScreen(navController: NavController, mainActivity: MainActivity, viewModel: TextFieldsViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue_200)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Text Fields",
                color = Color.Blue,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate(Constants.HOME_ROUTE) {
                            popUpTo(Constants.HOME_ROUTE)
                        }
                    }
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Use MutableState to represent TextField state.
                val text1: MutableState<String> = remember { mutableStateOf("") }
                val text2 = viewModel.text2
                val text3: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
                val text4: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }

                TextFieldStateful(label = "TextField Stateful", text = text1)

                TextFieldStateless(label = "TextField Stateless", text = text2, onTextChange = { input -> viewModel.onText2Changed(input) })

                OutlinedTextFieldEmail(text = text3)

                OutlinedTextFieldPassword(text = text4)

                Text(
                    text = "TextField Stateful: " + text1.value,
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "TextField Stateless: $text2",
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Outlined TextField Email: " + text3.value.text,
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Outlined TextField Password: " + text4.value.text,
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldsScreenPreview() {
    TextFieldsScreen(navController = rememberNavController(), mainActivity = MainActivity())
}
