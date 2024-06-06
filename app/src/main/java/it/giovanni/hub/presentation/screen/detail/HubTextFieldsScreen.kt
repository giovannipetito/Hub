package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import it.giovanni.hub.presentation.viewmodel.TextFieldsViewModel
import it.giovanni.hub.ui.items.TextFieldStateful
import it.giovanni.hub.ui.items.TextFieldStateless
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun HubTextFieldsScreen(
    navController: NavController,
    viewModel: TextFieldsViewModel = viewModel()
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.text_fields),
    topics = listOf("TextField Stateful", "TextField Stateless")
) {
    // Use MutableState to represent TextField state.
    val text1: MutableState<String> = remember { mutableStateOf("") }
    val text2: String = viewModel.text2

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = it)
    ) {
        item {
            TextFieldStateful(
                label = "TextField Stateful",
                text = text1
            )
        }

        item {
            TextFieldStateless(
                label = "TextField Stateless",
                text = text2,
                onTextChange = { input -> viewModel.onText2Changed(input) }
            )
        }

        item {
            Text(
                text = "TextField Stateful: " + text1.value,
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(height = 24.dp))

            Text(
                text = "TextField Stateless: $text2",
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextFieldsScreenPreview() {
    HubTextFieldsScreen(navController = rememberNavController())
}
