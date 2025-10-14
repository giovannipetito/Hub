package it.giovanni.hub.presentation.screen.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.presentation.viewmodel.ErrorHandlingViewModel
import it.giovanni.hub.ui.items.AlertBarContent
import it.giovanni.hub.ui.items.TextFieldStateful
import it.giovanni.hub.ui.items.buttons.HubButton
import it.giovanni.hub.ui.items.rememberAlertBarState
import it.giovanni.hub.utils.AlertBarPosition
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun ErrorHandlingScreen(
    navController: NavController,
    viewModel: ErrorHandlingViewModel = hiltViewModel()
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.error_handling),
    topics = listOf("HubResultPro")
) { paddingValues ->
    val context = LocalContext.current
    val state: AlertBarState = rememberAlertBarState()
    val input: MutableState<String> = remember { mutableStateOf("") }

    AlertBarContent(
        position = AlertBarPosition.BOTTOM,
        alertBarState = state,
        successMaxLines = 2,
        errorMaxLines = 2
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            item {
                TextFieldStateful(
                    label = "Password",
                    text = input
                )
            }

            item {
                HubButton(
                    modifier = Modifier,
                    text = "Check password",
                    onClick = {
                        viewModel.checkAndRegisterPassword(password = input.value, state = state)
                    }
                )
            }
        }
    }

    val result = viewModel.passwordError.value.getString(context = context)
    if (result != "")
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun ErrorHandlingScreen() {
    DefaultScreen(navController = rememberNavController())
}