package it.giovanni.hub.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.giovanni.hub.domain.result.simple.HubResult
import it.giovanni.hub.data.datasource.remote.UsersDataSource
import it.giovanni.hub.data.model.User
import it.giovanni.hub.data.response.UsersResponse
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val dataSource: UsersDataSource
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<User>>
        get() = _users

    val isRefreshing: MutableState<Boolean> = mutableStateOf(false)

    /**
     * Get data with Coroutines
     */
    fun fetchUsersWithCoroutines(page: Int, state: AlertBarState) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result: HubResult<UsersResponse> = dataSource.getCoroutinesUsers(page)) {
                is HubResult.Success<UsersResponse> -> {
                    if (result.data.users != null) {
                        state.addSuccess(success = "Loading successful!")
                        _users.value = result.data.users!!
                        addMockData()
                    }
                }
                is HubResult.Error -> {
                    state.addError(exception = Exception(result.message))
                }
            }
            delay(1000)
            isRefreshing.value = false
        }
    }

    /**
     * Get data with RxJava
     */
    fun fetchUsersWithRxJava(page: Int, state: AlertBarState) {
        val observable: Single<UsersResponse> = dataSource.getRxUsers(page)

        disposable = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    val users = response.users
                    if (users != null) {
                        state.addSuccess(success = "Loading successful!")
                        _users.value = users
                        addMockData()
                    }
                }, { error ->
                    state.addError(Exception(error.localizedMessage))
                    Log.e("[RX]", "error: " + error.localizedMessage)
                }
            )
    }

    private fun addMockData() {
        _users.value.forEach { user ->
            user.description = Constants.LOREM_IPSUM_LONG_TEXT
            user.badges = Constants.icons
        }
    }
}