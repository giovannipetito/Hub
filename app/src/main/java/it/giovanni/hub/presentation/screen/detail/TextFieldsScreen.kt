package it.giovanni.hub.presentation.screen.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.enums.SearchWidgetState
import it.giovanni.hub.ui.items.MainAppBar
import it.giovanni.hub.ui.items.OutlinedTextFieldEmail
import it.giovanni.hub.ui.items.OutlinedTextFieldPassword
import it.giovanni.hub.ui.items.TextFieldStateful
import it.giovanni.hub.ui.items.TextFieldStateless
import it.giovanni.hub.presentation.viewmodel.TextFieldsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TextFieldsScreen(navController: NavController, viewModel: TextFieldsViewModel = viewModel()) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = DataStoreRepository(context)
    val savedEmail = repository.getEmail().collectAsState(initial = "")

    // Use MutableState to represent TextField state.
    val text1: MutableState<String> = remember { mutableStateOf("") }
    val text2 = viewModel.text2
    val email: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
    val password: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }

    val searchWidgetState: State<SearchWidgetState> = viewModel.searchWidgetState
    val searchTextState: State<String> = viewModel.searchTextState

    Scaffold(
        topBar = {
            MainAppBar(
                searchWidgetState = searchWidgetState.value,
                searchTextState = searchTextState.value,
                onTextChange = {
                    viewModel.updateSearchTextState(newValue = it)
                },
                onCloseClicked = {
                    viewModel.updateSearchTextState(newValue = "")
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    Log.d("[SEARCH]", it)
                },
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                TextFieldStateful(label = "TextField Stateful", text = text1)

                TextFieldStateless(label = "TextField Stateless", text = text2, onTextChange = { input -> viewModel.onText2Changed(input) })

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedTextFieldEmail(email = email)

                    IconButton(onClick = {
                        scope.launch(Dispatchers.IO) {
                            repository.saveEmail(email = email.value.text)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite Icon",
                            tint = Color.Red
                        )
                    }
                }

                OutlinedTextFieldPassword(password = password)

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
                    text = "Outlined TextField Email: " + email.value.text,
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Outlined TextField Password: " + password.value.text,
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Saved Email: " + savedEmail.value,
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
    TextFieldsScreen(navController = rememberNavController())
}
