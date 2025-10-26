package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.repositoryimpl.local.RoomRepository
import it.giovanni.hub.data.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomCoroutinesViewModel @Inject constructor(
    private val repository: RoomRepository
): ViewModel() {

    private val _users: MutableStateFlow<List<UserEntity>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<UserEntity>> = _users.asStateFlow()

    private var _userById: MutableState<UserEntity>? = mutableStateOf(UserEntity(0, "", "", ""))
    val userById: State<UserEntity>? = _userById

    init {
        readUsers()
    }

    fun createUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createUser(userEntity = userEntity)
            readUsers() // Needed to update the UI.
        }
    }

    private fun readUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _users.value = repository.readUsers()
        }
    }

    fun readUserById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _userById?.value = repository.readUserById(id = id)
            readUsers() // Needed to update the UI.
        }
    }

    fun updateUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(userEntity = userEntity)
            readUsers() // Needed to update the UI.
        }
    }

    fun deleteUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUsers()
            readUsers() // Needed to update the UI.
        }
    }

    fun deleteUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(userEntity = userEntity)
            readUsers() // Needed to update the UI.
        }
    }
}