package it.giovanni.hub.presentation.screen.detail.gemini

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.screen.detail.BaseScreen
import it.giovanni.hub.presentation.viewmodel.gemini.TextInputViewModel
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun TextInputScreen(navController: NavController) {

    val topics: List<String> = listOf("Generate text from text-only input")

    val viewModel: TextInputViewModel = viewModel()

    var prompt: String by remember { mutableStateOf("") }

    var isStreaming by remember { mutableStateOf(false) }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.text_input),
        topics = topics
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Switch(
                        checked = isStreaming,
                        onCheckedChange = { isStreaming = it }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = "Streaming",
                        fontWeight = FontWeight.Bold,
                        color = if (isStreaming)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize
                    )
                }
            }

            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    value = prompt,
                    onValueChange = { prompt = it },
                    label = { Text("Prompt") },
                    placeholder = { Text("Ask to Gemini") },
                    singleLine = true
                )
            }

            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    onClick = {
                        if (isStreaming) {
                            viewModel.generateContentStream(prompt)
                        } else {
                            viewModel.generateContent(prompt)
                        }
                    },
                    enabled = prompt.isNotEmpty()
                ) {
                    Text("Generate content")
                }
            }

            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = viewModel.responseText.value ?: ""
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextInputScreenPreview() {
    TextInputScreen(navController = rememberNavController())
}