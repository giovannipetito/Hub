package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.datasource.local.RoomRepository
import it.giovanni.hub.domain.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val repository: RoomRepository
): ViewModel() {

    private val _users: MutableStateFlow<List<UserEntity>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<UserEntity>> = _users.asStateFlow()

    var user: UserEntity by mutableStateOf(UserEntity(0, "", "", ""))
        private set

    private var deletedUser: UserEntity? = null

    fun insertUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(userEntity = userEntity)
        }
    }

    fun updateUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(userEntity = userEntity)
        }
    }

    fun deleteUser(userEntity: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            deletedUser = userEntity
            repository.deleteUser(userEntity = userEntity)
        }
    }

    fun undoDeleteUser() {
        deletedUser?.let { userEntity ->
            viewModelScope.launch(Dispatchers.IO) {
                insertUser(userEntity = userEntity)
            }
        }
    }

    fun getUserById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            user = repository.getUserById(id = id)
        }
    }

    fun getUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            _users.value = repository.getUsers()
        }
    }

    fun deleteUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUsers()
        }
    }
}