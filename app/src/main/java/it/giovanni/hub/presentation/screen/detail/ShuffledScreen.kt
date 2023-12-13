package it.giovanni.hub.presentation.screen.detail

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.ui.items.Text2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShuffledScreen(navController: NavController) {

    var languages: List<String> by remember {
        mutableStateOf(listOf("Kotlin", "Java", "Python", "Swift", "JavaScript", "Dart"))
    }

    val showDialog = remember { mutableStateOf(false) }
    val items = listOf("Item 1", "Item 2", "Item 3")

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.shuffled_items),
        actions = {
            IconButton(onClick = {
                showDialog.value = true
            }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Info"
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            InfoDialog(items = items, showDialog = showDialog)
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = languages,
                    key = {it}
                ) { item: String ->
                    Text2(
                        text = item,
                        modifier = Modifier
                            .animateItemPlacement(
                                animationSpec = tween(durationMillis = 600)
                            )
                    )
                }
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        onClick = {
                            languages = languages.shuffled()
                        }
                    ) {
                        Text("Shuffle")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoDialog(items: List<String>, showDialog: MutableState<Boolean>) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Info") },
            text = {
                Column {
                    items.forEach { item ->
                        Text(text = item)
                    }
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Dismiss")
                }
            },
            confirmButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Confirm")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShuffledScreenPreview() {
    ShuffledScreen(navController = rememberNavController())
}