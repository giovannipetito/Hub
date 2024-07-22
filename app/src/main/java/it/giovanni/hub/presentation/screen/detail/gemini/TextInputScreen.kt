package it.giovanni.hub.presentation.screen.detail.gemini

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.presentation.viewmodel.TextInputViewModel
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun TextInputScreen(navController: NavController) {

    val topics: List<String> = listOf("Generate text from text-only input")

    val viewModel: TextInputViewModel = viewModel()
    var prompt: String? by remember { mutableStateOf("") }
    var responseText: String? by remember { mutableStateOf("") }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.text_input),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {
                TextField(
                    value = prompt!!,
                    onValueChange = { prompt = it },
                    label = { Text("Enter prompt") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Button(
                    onClick = {
                        responseText = viewModel.generateContent(prompt)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Generate Content")
                }
            }
            item {
                Text(text = responseText!!)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextInputScreenPreview() {
    TextInputScreen(navController = rememberNavController())
}