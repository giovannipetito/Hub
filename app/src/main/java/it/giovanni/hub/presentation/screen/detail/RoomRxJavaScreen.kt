package it.giovanni.hub.presentation.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import it.giovanni.hub.R
import it.giovanni.hub.data.entity.UserEntity
import it.giovanni.hub.presentation.viewmodel.RoomRxJavaViewModel
import it.giovanni.hub.ui.items.ExpandableRoomFAB
import it.giovanni.hub.ui.items.HubAlertDialog
import it.giovanni.hub.ui.items.TextFieldsDialog
import it.giovanni.hub.ui.items.cards.RoomItem
import it.giovanni.hub.utils.Globals.getContentPadding

@Composable
fun RoomRxJavaScreen(
    navController: NavController,
    viewModel: RoomRxJavaViewModel = hiltViewModel()
) {
    var searchResult: String by remember { mutableStateOf("") }

    BaseScreen(
        navController = navController,
        title = stringResource(id = R.string.room_database_rxjava),
        topics = listOf("Room Database"),
        search = true,
        placeholder = "Search user by Id...",
        onSearchResult = { result ->
            searchResult = result
        }
    ) { paddingValues ->

        val users: List<UserEntity> by viewModel.users.collectAsState()

        val showCreateUserDialog = remember { mutableStateOf(false) }
        val showUpdateUserDialog = remember { mutableStateOf(false) }
        val showDeleteUserDialog = remember { mutableStateOf(false) }
        val showDeleteUsersDialog = remember { mutableStateOf(false) }

        val id: MutableState<Int> = remember { mutableIntStateOf(0) }
        val firstName: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
        val lastName: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }
        val age: MutableState<TextFieldValue> = remember { mutableStateOf(TextFieldValue("")) }

        /*
        LaunchedEffect(Unit) {
            viewModel.readUsers()
        }
        */

        fun canParseToInt(input: String): Boolean {
            return input.toIntOrNull() != null
        }

        if (canParseToInt(input = searchResult)) {
            viewModel.readUserById(id = searchResult.toInt())
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

        ExpandableRoomFAB(
            paddingValues = paddingValues,
            users = users,
            onShowCreateUserDialog = {
                showCreateUserDialog.value = it
            },
            onShowDeleteUsersDialog = {
                showDeleteUsersDialog.value = it
            },
            onResetUserInfo = {
                resetUserInfo()
            }
        )

        TextFieldsDialog(
            icon = painterResource(id = R.drawable.ico_add_user),
            title = "Create User",
            text = "Confirm you want to create this user?",
            firstName = firstName,
            lastName = lastName,
            age = age,
            dismissButtonText = "Dismiss",
            confirmButtonText = "Create",
            showDialog = showCreateUserDialog,
            onDismissRequest = {
                showCreateUserDialog.value = false
                resetUserInfo()
            },
            onConfirmation = {
                showCreateUserDialog.value = false
                viewModel.createUser(
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
            icon = painterResource(id = R.drawable.ico_edit),
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
            icon = painterResource(id = R.drawable.ico_delete_user),
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

        HubAlertDialog(
            icon = painterResource(id = R.drawable.ico_delete),
            title = "Delete Users",
            text = "Confirm you want to delete all the users?",
            dismissButtonText = "Dismiss",
            confirmButtonText = "Delete",
            showDialog = showDeleteUsersDialog,
            onDismissRequest = {
                showDeleteUsersDialog.value = false
                resetUserInfo()
            },
            onConfirmation = {
                showDeleteUsersDialog.value = false
                viewModel.deleteUsers()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoomRxJavaScreenPreview() {
    RoomRxJavaScreen(navController = rememberNavController())
}