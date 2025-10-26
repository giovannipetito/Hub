package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.giovanni.hub.domain.result.simple.HubResult
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.domain.usecase.GetCoroutinesUsersUseCase
import it.giovanni.hub.domain.usecase.GetRxJavaUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getCoroutinesUsers: GetCoroutinesUsersUseCase,
    private val getRxJavaUsers: GetRxJavaUsersUseCase
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
            when (val result = getCoroutinesUsers(page)) {
                is HubResult.Success -> {
                    _users.value = result.data
                    onResult(Result.success(Unit))
                }
                is HubResult.Error -> {
                    onResult(Result.failure(Exception(result.message)))
                }
            }
        }
    }

    /**
     * Get data with RxJava (don't wrap Rx errors into HubResult)
     */
    /*
    fun fetchUsersWithRxJava(page: Int, onResult: (Result<Unit>) -> Unit) {
        disposable = getRxJavaUsers(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
    */

    /**
     * Get data with RxJava (wrap Rx errors into HubResult)
     */
    fun fetchUsersWithRxJava(page: Int, onResult: (Result<Unit>) -> Unit) {
        disposable = getRxJavaUsers(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                when (result) {
                    is HubResult.Success -> {
                        _users.value = result.data
                        onResult(Result.success(Unit))
                    }
                    is HubResult.Error -> {
                        onResult(Result.failure(Exception(result.message)))
                    }
                }
            }, { throwable ->
                // Safety net for unexpected runtime errors
                onResult(Result.failure(throwable))
            })
    }
}