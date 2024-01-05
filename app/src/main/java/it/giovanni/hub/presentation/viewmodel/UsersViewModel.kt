package it.giovanni.hub.presentation.viewmodel

import android.util.Log
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
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val dataSource: DataSource
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<User>>
        get() = _users

    /**
     * Get data with Coroutines
     */
    fun fetchUsersWithCoroutines(page: Int, state: AlertBarState) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result: HubResult<UsersResponse> = dataSource.getUsers(page)) {
                is HubResult.Success<UsersResponse> -> {
                    if (result.data.users != null) {
                        state.addSuccess(message = "Loading successful!")
                        _users.value = result.data.users!!
                        addMockedData()
                    }
                }
                is HubResult.Error -> {
                    state.addError(Exception(result.message))
                }
            }
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
                        state.addSuccess(message = "Loading successful!")
                        _users.value = users
                        addMockedData()
                    }
                }, { error ->
                    state.addError(Exception(error.localizedMessage))
                    Log.e("[RX]", "error: " + error.localizedMessage)
                }
            )
    }

    private fun addMockedData() {
        _users.value.forEach { user ->
            user.description = Constants.loremIpsumLongText
            user.badges = Constants.icons
        }
    }
}