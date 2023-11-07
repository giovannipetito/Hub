package it.giovanni.hub.presentation.viewmodel

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import it.giovanni.hub.data.HubResult
import it.giovanni.hub.data.datasource.remote.DataSource
import it.giovanni.hub.data.model.User
import it.giovanni.hub.data.response.UsersResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val dataSource: DataSource
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<User>>
        get() = _users

    private val _rxUsers: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val rxUsers: StateFlow<List<User>>
        get() = _rxUsers

    /**
     * Get data with Coroutines
     */
    fun fetchUsersWithCoroutines(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result: HubResult<UsersResponse> = dataSource.getUsers(page)) {
                is HubResult.Success<UsersResponse> -> {
                    if (result.data.users != null) {
                        _users.value = result.data.users!!
                    }
                }
                is HubResult.Error -> {
                    // todo: show error message
                }
            }
        }
    }

    /**
     * Get data with RxJava
     */
    fun fetchUsersWithRxJava(page: Int) {
        val observable: Single<UsersResponse> = dataSource.getRxUsers(page)

        disposable = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    val users = response.users
                    if (users != null) {
                        _rxUsers.value = users
                        addMockedData()
                    }
                }, { error ->
                    Log.e("[RX]", "error: " + error.message)
                }
            )
    }

    private fun addMockedData() {
        _rxUsers.value.forEach { user ->
            user.description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                    "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim " +
                    "veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo " +
                    "consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
                    "dolore eu fugiat nulla pariatur."
            user.badges = listOf(
                Icons.Default.Check,
                Icons.Default.Edit,
                Icons.Default.Face,
                Icons.Default.Email,
                Icons.Default.List,
                Icons.Default.Home
            )
        }
    }
}