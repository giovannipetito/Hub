package it.giovanni.hub.presentation.screen.detail

import android.util.Log
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
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
import it.giovanni.hub.utils.Globals.getContentPadding
import it.giovanni.hub.utils.Globals.getFloatingActionButtonPadding

@Composable
fun RoomScreen(
    navController: NavController,
    viewModel: RoomViewModel = hiltViewModel()
) {
    var searchResult: String by remember { mutableStateOf("") }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.room_database),
        topics = listOf("Room Database"),
        showSearch = true,
        placeholder = "Search user by Id...",
        onSearchResult = { result ->
            searchResult = result
        }
    ) { paddingValues ->

        val users: List<UserEntity> by viewModel.users.collectAsState()

        val showInsertUserDialog = remember { mutableStateOf(false) }
        val showUpdateUserDialog = remember { mutableStateOf(false) }
        val showDeleteUserDialog = remember { mutableStateOf(false) }

        val id: MutableState<Int> = remember { mutableIntStateOf(0) }
        val firstName: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
        val lastName: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
        val age: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }

        LaunchedEffect(Unit) {
            viewModel.getUsers()
        }

        fun canParseToInt(input: String): Boolean {
            return input.toIntOrNull() != null
        }

        if (canParseToInt(input = searchResult)) {
            viewModel.getUserById(id = searchResult.toInt())
        }

        fun resetUserInfo() {
            id.value = 0
            firstName.value = TextFieldValue("")
            lastName.value = TextFieldValue("")
            age.value = TextFieldValue("")
        }

        fun validateUserInfo(user: UserEntity) {
            id.value = user.id
            firstName.value = TextFieldValue(user.firstName)
            lastName.value = TextFieldValue(user.lastName)
            age.value = TextFieldValue(user.age)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = getContentPadding(paddingValues = paddingValues)
        ) {
            items(users) { user ->
                RoomItem(
                    user = user,
                    isUserById = user.id == viewModel.userById?.value?.id,
                    onEditClick = {
                        showUpdateUserDialog.value = true
                        validateUserInfo(user = user)
                    },
                    onDeleteClick = {
                        showDeleteUserDialog.value = true
                        validateUserInfo(user = user)
                    }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(paddingValues = getFloatingActionButtonPadding(paddingValues = paddingValues))
                    .align(Alignment.BottomEnd),
                onClick = {
                    showInsertUserDialog.value = true
                    resetUserInfo()
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
            text = "Confirm you want to insert this user?",
            firstName = firstName,
            lastName = lastName,
            age = age,
            dismissButtonText = "Dismiss",
            confirmButtonText = "Insert",
            showDialog = showInsertUserDialog,
            onDismissRequest = {
                showInsertUserDialog.value = false
                resetUserInfo()
            },
            onConfirmation = {
                showInsertUserDialog.value = false
                viewModel.insertUser(
                    userEntity = UserEntity(
                        id = id.value,
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        age = age.value.text
                    )
                )
            }
        )

        TextFieldsDialog(
            icon = Icons.Default.Edit,
            title = "Update User",
            text = "Confirm you want to update this user?",
            firstName = firstName,
            lastName = lastName,
            age = age,
            dismissButtonText = "Dismiss",
            confirmButtonText = "Update",
            showDialog = showUpdateUserDialog,
            onDismissRequest = {
                showUpdateUserDialog.value = false
                resetUserInfo()
            },
            onConfirmation = {
                showUpdateUserDialog.value = false
                viewModel.updateUser(
                    userEntity = UserEntity(
                        id = id.value,
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        age = age.value.text
                    )
                )
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
                resetUserInfo()
            },
            onConfirmation = {
                showDeleteUserDialog.value = false
                viewModel.deleteUser(
                    userEntity = UserEntity(
                        id = id.value,
                        firstName = firstName.value.text,
                        lastName = lastName.value.text,
                        age = age.value.text
                    )
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoomScreenPreview() {
    RoomScreen(navController = rememberNavController())
}