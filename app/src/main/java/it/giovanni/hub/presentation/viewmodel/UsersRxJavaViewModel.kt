package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.domain.result.simple.HubResult
import it.giovanni.hub.domain.usecase.GetRxJavaUsersUseCase
import it.giovanni.hub.domain.usecase.SearchParams
import it.giovanni.hub.domain.usecase.SearchRxJavaUsersUseCase
import it.giovanni.hub.domain.usecase.SortBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UsersRxJavaViewModel @Inject constructor(
    private val getUsersUseCase: GetRxJavaUsersUseCase,
    private val searchUsersUseCase: SearchRxJavaUsersUseCase
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<User>>
        get() = _users

    /**
     * Get data with RxJava (don't wrap Rx errors into HubResult)
     */
    /*
    fun fetchRxJavaUsers(page: Int, onResult: (Result<Unit>) -> Unit) {
        disposable = getUsersUseCase(page)
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
    fun fetchRxJavaUsers(page: Int, onResult: (Result<Unit>) -> Unit) {
        disposable = getUsersUseCase(page)
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

    fun searchRxJavaUsers(
        page: Int,
        query: String,
        sortBy: SortBy,
        ascending: Boolean,
        onResult: (Result<Unit>) -> Unit
    ) {
        // Cancel any previous Rx job for this VM
        disposable?.dispose()

        val params = SearchParams(page, query, sortBy, ascending)
        disposable = searchUsersUseCase(params)
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
            }, { t ->
                // Safety net in case something bubbles up unexpectedly
                onResult(Result.failure(t))
            })
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}