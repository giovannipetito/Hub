package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.giovanni.hub.domain.result.simple.HubResult
import it.giovanni.hub.domain.repositoryint.remote.UsersRepository
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.data.response.UsersResponse
import it.giovanni.hub.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.withContext

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UsersRepository
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<User>>
        get() = _users

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    /**
     * Get data with Coroutines
     */
    fun fetchUsersWithCoroutines(page: Int, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            _isRefreshing.value = true

            // Run the network/data call on IO
            val result: Result<HubResult<UsersResponse>> =
                runCatching {
                    withContext(Dispatchers.IO) {
                        repository.getCoroutinesUsers(page)
                    }
                }

            // If the call itself threw, report and stop
            val hubResult = result.getOrElse { throwable ->
                onResult(Result.failure(throwable))
                _isRefreshing.value = false
                return@launch
            }

            when (hubResult) {
                is HubResult.Success -> {
                    val users = hubResult.data.users.orEmpty()
                    _users.value = addMockData(users = users)
                    onResult(Result.success(Unit))
                }
                is HubResult.Error -> {
                    onResult(Result.failure(Exception(hubResult.message)))
                }
            }

            _isRefreshing.value = false
        }
    }

    /**
     * Get data with RxJava
     */
    fun fetchUsersWithRxJava(page: Int, onResult: (Result<Unit>) -> Unit) {
        disposable = repository.getRxJavaUsers(page)
            .subscribeOn(Schedulers.io())
            .map { response -> addMockData(users = response.users.orEmpty()) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isRefreshing.value = true }
            .doFinally { _isRefreshing.value = false }
            .subscribe(
                { users ->
                    _users.value = users
                    onResult(Result.success(Unit))
                },
                { error ->
                    onResult(Result.failure(error))
                }
            )
    }

    private fun addMockData(users: List<User>): List<User> =
        users.map { user ->
            user.copy(
                description = Constants.LOREM_IPSUM_LONG_TEXT,
                badgeIds = Constants.ICON_IDS
            )
        }
}