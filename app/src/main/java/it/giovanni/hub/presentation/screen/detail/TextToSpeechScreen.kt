package it.giovanni.hub.presentation.screen.detail

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.TextToSpeechViewModel
import it.giovanni.hub.ui.items.ClickableListDialog
import it.giovanni.hub.utils.Globals.getContentPadding
import java.util.Locale

@Composable
fun TextToSpeechScreen(navController: NavController) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.text_to_speech),
    topics = listOf("TextToSpeech")
) { paddingValues ->

    val context = LocalContext.current
    val viewModel: TextToSpeechViewModel = viewModel()

    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var useExternalStorage by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

    var selectedLanguage: Locale by remember { mutableStateOf(Locale.ITALY) }
    val languages = mapOf(
        "Canada" to Locale.CANADA,
        "Canada French" to Locale.CANADA_FRENCH,
        "Chinese" to Locale.CHINA,
        "English (UK)" to Locale.UK,
        "English (US)" to Locale.US,
        "French" to Locale.FRANCE,
        "German" to Locale.GERMANY,
        "Italian" to Locale.ITALY,
        "Japanese" to Locale.JAPAN,
        "Korean" to Locale.KOREA,
        "Portuguese" to Locale("pt", "PT"),
        "Spanish" to Locale("es", "ES"),
        "Taiwan" to Locale.TAIWAN
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        item {
            Spacer(modifier = Modifier.height(0.dp))
        }

        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                value = text,
                onValueChange = { text = it },
                label = { Text("Text to speech") },
                placeholder = { Text("Enter the text") },
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.speakText(
                                text = text,
                                language = selectedLanguage
                            )
                        },
                        enabled = text.isNotEmpty()
                    ) {
                        Icon(
                            modifier = Modifier.size(size = 24.dp),
                            painter = painterResource(id = R.drawable.ico_microphone),
                            contentDescription = "Microphone",
                            tint = if (text.isNotEmpty()) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            )
        }

        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = languages.entries.find { it.value == selectedLanguage }?.key ?: "",
                    onValueChange = {},
                    label = { Text("Language") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }
                    }
                )
                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    languages.forEach { (languageName, languageCode) ->

                        DropdownMenuItem(
                            onClick = {
                                selectedLanguage = languageCode
                                expanded = false
                            },
                            text = { Text(text = languageName) }
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    isSaving = true
                    viewModel.saveSpeechToFile(
                        text = text,
                        language = selectedLanguage,
                        fileName = System.currentTimeMillis().toString(),
                        useExternalStorage = useExternalStorage,
                        onComplete = { success, file ->
                            isSaving = false
                            if (success && file != null) {
                                viewModel.setFile(file)
                                showDialog.value = true
                            } else {
                                Toast.makeText(context, "Failed to save file", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                },
                enabled = text.isNotEmpty() && !isSaving
            ) {
                Text(if (isSaving) "Saving..." else "Save speech to file")
            }
        }

        item {
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = useExternalStorage,
                    onCheckedChange = { useExternalStorage = it }
                )
                Text("Save file to external storage")
            }
        }
    }

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            val path = it.path
            Toast.makeText(context, "Selected Directory: $path", Toast.LENGTH_LONG).show()
        }
    }

    val list: List<String> =
        if (useExternalStorage) listOf("Show File Dialog", "Share File", "Open File Directory")
        else listOf("Show File Dialog", "Share File")

    val itemClickActions: List<() -> Unit> = listOf(
        { viewModel.showFileDialog(context, viewModel.file.value) },
        { viewModel.shareFile(context, viewModel.file.value) },
        { activityResultLauncher.launch(null) },
        // { navigateToPublicDirectory(context) },
    )

    ClickableListDialog(
        title = "File saved successfully",
        list = list,
        confirmButtonText = "Close",
        showDialog = showDialog,
        onConfirmation = { showDialog.value = false },
        itemClickActions = itemClickActions
    )
}

fun navigateToPublicDirectory(context: Context) {
    val publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        publicDirectory
    )

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "*/*")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No application found to open the directory", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun TextToSpeechScreenPreview() {
    TextToSpeechScreen(navController = rememberNavController())
}