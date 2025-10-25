package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.giovanni.hub.data.repositoryimpl.local.RoomRepository
import it.giovanni.hub.domain.entity.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoomRxJavaViewModel @Inject constructor(
    private val repository: RoomRepository
): ViewModel() {

    private var disposable: Disposable? = null

    private val _users: MutableStateFlow<List<UserEntity>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<UserEntity>> = _users.asStateFlow()

    private var _userById: MutableState<UserEntity>? = mutableStateOf(UserEntity(0, "", "", ""))
    val userById: State<UserEntity>? = _userById

    init {
        readUsers()
    }

    fun createUser(userEntity: UserEntity) {
        repository.createRxJavaUser(userEntity = userEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        readUsers() // Needed to update the UI.
    }

    private fun readUsers() {
        disposable = repository.readRxJavaUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { users ->
                _users.value = users
            }
    }

    fun readUserById(id: Int) {
        disposable = repository.readRxJavaUserById(id = id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ user ->
                _userById?.value = user
            }

        readUsers() // Needed to update the UI.
    }

    fun updateUser(userEntity: UserEntity) {
        repository.updateRxJavaUser(userEntity = userEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        readUsers() // Needed to update the UI.
    }

    fun deleteUsers() {
        repository.deleteRxJavaUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun deleteUser(userEntity: UserEntity) {
        repository.deleteRxJavaUser(userEntity = userEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        readUsers() // Needed to update the UI.
    }
}