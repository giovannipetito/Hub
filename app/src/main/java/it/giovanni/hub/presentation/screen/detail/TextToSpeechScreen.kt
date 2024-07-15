package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.TextToSpeechViewModel
import it.giovanni.hub.utils.Globals.getContentPadding
import java.util.Locale

@Composable
fun TextToSpeechScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.text_to_speech),
    topics = listOf("TextToSpeech")
) { paddingValues ->

    val viewModel: TextToSpeechViewModel = viewModel()

    var text by remember { mutableStateOf("") }

    var selectedLanguage: Locale by remember { mutableStateOf(Locale.ITALY) }
    val languages = mapOf(
        "English (US)" to Locale.US,
        "English (UK)" to Locale.UK,
        "Italian" to Locale.ITALY,
        "French" to Locale.FRANCE,
        "German" to Locale.GERMANY
        // Add more languages as needed
    )

    var expanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        item {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter text to speak") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Button(
                onClick = {
                    viewModel.speakText(
                        text = text,
                        language = selectedLanguage
                    )
                }
            ) {
                Text("Speak")
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = languages.entries.find { it.value == selectedLanguage }?.key ?: "",
                    onValueChange = {},
                    label = { Text("Language") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    languages.forEach { (languageName, languageCode) ->

                        DropdownMenuItem(
                            onClick = {
                                selectedLanguage = languageCode
                                expanded = false
                            },
                            text = { Text(languageName) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextToSpeechScreenPreview() {
    TextToSpeechScreen(navController = rememberNavController())
}