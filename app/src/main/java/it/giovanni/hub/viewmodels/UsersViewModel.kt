package it.giovanni.hub.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import it.giovanni.hub.User
import it.giovanni.hub.HubResult
import it.giovanni.hub.UsersResponse
import it.giovanni.hub.datasource.UsersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val usersDataSource: UsersDataSource): ViewModel() {

    private var disposable: Disposable? = null

    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList<User>())
    val users: StateFlow<List<User>>
        get() = _users

    private val _rxUsers: MutableStateFlow<List<User>> = MutableStateFlow(emptyList<User>())
    val rxUsers: StateFlow<List<User>>
        get() = _rxUsers

    /**
     * Get data with Paging & Coroutines
     */
    fun fetchUsers(page: Int) {
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
    fun fetchRxUsers(page: Int) {
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
}