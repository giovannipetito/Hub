package it.giovanni.hub.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import it.giovanni.hub.data.model.User
import it.giovanni.hub.data.HubResult
import it.giovanni.hub.data.response.UsersResponse
import it.giovanni.hub.data.datasource.UsersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val usersDataSource: UsersDataSource): ViewModel() {

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
            when (val result: HubResult<UsersResponse> = usersDataSource.getUsers(page)) {
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
        val observable: Single<UsersResponse> = usersDataSource.getRxUsers(page)

        disposable = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    val users = response.users
                    if (users != null)
                        _rxUsers.value = users
                }, { error ->
                    Log.e("[RX]", "error: " + error.message)
                }
            )
    }

    /**
     * Get data with Paging
     */
    fun fetchCharactersWithPaging(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result: HubResult<UsersResponse> = usersDataSource.getUsers(page)) {
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
}