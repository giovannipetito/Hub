package it.giovanni.hub.presentation.screen.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.rotate
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
        val showDeleteUsersDialog = remember { mutableStateOf(false) }

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

        ExpandableFAB(
            paddingValues = paddingValues,
            onShowInsertUserDialog = {
                showInsertUserDialog.value = it
            },
            onShowDeleteUsersDialog = {
                showDeleteUsersDialog.value = it
            },
            onResetUserInfo = {
                resetUserInfo()
            }
        )

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

        HubAlertDialog(
            icon = Icons.Default.Delete,
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

@Composable
fun ExpandableFAB(
    paddingValues: PaddingValues,
    onShowInsertUserDialog: (Boolean) -> Unit,
    onShowDeleteUsersDialog: (Boolean) -> Unit,
    onResetUserInfo: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotateAnimation = animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = getFloatingActionButtonPadding(paddingValues = paddingValues)),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        onShowInsertUserDialog(true)
                        onResetUserInfo()
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Icon")
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        onShowDeleteUsersDialog(true)
                        onResetUserInfo()
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Icon")
                }
            }

            FloatingActionButton(
                onClick = {
                    isExpanded = !isExpanded
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(
                    modifier = Modifier.rotate(degrees = rotateAnimation.value),
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "MoreVert Icon"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomScreenPreview() {
    RoomScreen(navController = rememberNavController())
}