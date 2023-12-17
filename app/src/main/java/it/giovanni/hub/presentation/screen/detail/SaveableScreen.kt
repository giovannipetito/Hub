package it.giovanni.hub.presentation.screen.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.model.Person

@Composable
fun SaveableScreen(navController: NavController) {

    val topics: List<String> = listOf("rememberSaveable")

    var person by rememberSaveable {
        mutableStateOf(Person("", "", false))
    }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.saveable),
        topics = topics
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun SaveableScreenPreview() {
    SaveableScreen(navController = rememberNavController())
}