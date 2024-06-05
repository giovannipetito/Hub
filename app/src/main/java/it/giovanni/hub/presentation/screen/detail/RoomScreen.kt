package it.giovanni.hub.presentation.screen.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.domain.entity.UserEntity
import it.giovanni.hub.presentation.viewmodel.RoomViewModel
import it.giovanni.hub.ui.items.HubAlertDialog
import it.giovanni.hub.ui.items.TextFieldsDialog
import it.giovanni.hub.ui.items.cards.RoomItem
import it.giovanni.hub.utils.Constants.NAVIGATION_BAR_HEIGHT
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun RoomScreen(
    navController: NavController,
    viewModel: RoomViewModel = hiltViewModel()
) = BaseScreen(
    navController = navController,
    title = stringResource(id = R.string.room_database),
    topics = listOf("Room Database")
) { paddingValues ->

    LaunchedEffect(Unit) {
        viewModel.insertUser(
            userEntity = UserEntity(
                id = 1,
                firstName = "Giovanni",
                lastName = "Petito",
                age = "36"
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getUsers()
    }

    val showInsertUserDialog = remember { mutableStateOf(false) }
    val showUpdateUserDialog = remember { mutableStateOf(false) }
    val showDeleteUserDialog = remember { mutableStateOf(false) }

    val users: List<UserEntity> by viewModel.users.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = getContentPadding(paddingValues = paddingValues)
    ) {
        items(users) { user ->
            RoomItem(
                user = user,
                onEditClick = {
                    showUpdateUserDialog.value = true
                },
                onDeleteClick = {
                    showDeleteUserDialog.value = true
                }
            )
        }
    }

    val orientation: Int = LocalConfiguration.current.orientation

    val bottomPadding =
        if (orientation == Configuration.ORIENTATION_PORTRAIT) NAVIGATION_BAR_HEIGHT + 12.dp
        else 12.dp

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(bottom = bottomPadding, end = 16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                showInsertUserDialog.value = true
            },
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
        ) {
            Icon(imageVector = Icons.Filled.Add, "Add")
        }
    }

    TextFieldsDialog(
        icon = Icons.Default.Person,
        title = "Insert User",
        text = "Confirm you want to insert a new user?",
        dismissButtonText = "Dismiss",
        confirmButtonText = "Insert",
        showDialog = showInsertUserDialog,
        onDismissRequest = {
            showInsertUserDialog.value = false
        },
        onConfirmation = {
            showInsertUserDialog.value = false
            viewModel.insertUser(userEntity = viewModel.user)
        }
    )

    TextFieldsDialog(
        icon = Icons.Default.Edit,
        title = "Update User",
        text = "Confirm you want to update this user?",
        dismissButtonText = "Dismiss",
        confirmButtonText = "Update",
        showDialog = showUpdateUserDialog,
        onDismissRequest = {
            showUpdateUserDialog.value = false
        },
        onConfirmation = {
            showUpdateUserDialog.value = false
            viewModel.updateUser(userEntity = viewModel.user)
        }
    )

    HubAlertDialog(
        icon = Icons.Default.Delete,
        title = "Delete User",
        text = "Confirm you want to delete this user?",
        dismissButtonText = "Dismiss",
        confirmButtonText = "Delete",
        showDialog = showDeleteUserDialog,
        onDismissRequest = {
            showDeleteUserDialog.value = false
        },
        onConfirmation = {
            showDeleteUserDialog.value = false
            viewModel.deleteUser(userEntity = viewModel.user)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RoomScreenPreview() {
    RoomScreen(navController = rememberNavController())
}