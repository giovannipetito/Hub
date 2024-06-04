package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.presentation.viewmodel.RealmViewModel
import it.giovanni.hub.ui.items.ListDialog
import it.giovanni.hub.ui.items.cards.CourseItem
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun RealmScreen(
    navController: NavController,
    viewModel: RealmViewModel = viewModel()
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.realm),
    topics = listOf("Realm database")
) { paddingValues ->
    val courses by viewModel.courses.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        items(courses) { course ->
            CourseItem(
                modifier = Modifier.clickable { viewModel.showCourse(course) },
                course = course
            )
        }
    }

    val showDialog = remember { mutableStateOf(false) }
    showDialog.value = viewModel.course != null

    val addresses = remember { mutableStateListOf<String>() }

    viewModel.course?.teacher?.address?.let { address ->
        addresses.add(address.fullName)
        addresses.add(address.street + " " + address.houseNumber)
        addresses.add(address.zip.toString() + " " + address.city)
    }

    ListDialog(
        title = "Teacher Info",
        list = addresses,
        confirmButtonText = "Delete",
        showDialog = showDialog,
        onConfirmation = {
            // viewModel.hideCourse()
            viewModel.deleteCourse()
            showDialog.value = false
            addresses.clear()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RealmScreenPreview() {
    RealmScreen(navController = rememberNavController())
}