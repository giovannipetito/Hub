package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.domain.result.simple.HubResult
import it.giovanni.hub.domain.usecase.GetCoroutinesUsersUseCase
import it.giovanni.hub.domain.usecase.SearchParams
import it.giovanni.hub.domain.usecase.SearchCoroutinesUsersUseCase
import it.giovanni.hub.domain.usecase.SortBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersCoroutinesViewModel @Inject constructor(
    private val getUsersUseCase: GetCoroutinesUsersUseCase,
    private val searchUsersUseCase: SearchCoroutinesUsersUseCase,
) : ViewModel() {

    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<User>>
        get() = _users

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    /**
     * Get data with Coroutines
     */
    fun fetchCoroutinesUsers(page: Int, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            when (val result = getUsersUseCase(page)) {
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

    fun searchCoroutinesUsers(
        page: Int,
        query: String,
        sortBy: SortBy,
        ascending: Boolean,
        onResult: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            val params = SearchParams(page, query, sortBy, ascending)
            when (val res = searchUsersUseCase(params)) {
                is HubResult.Error -> onResult(Result.failure(Exception(res.message)))
                is HubResult.Success -> {
                    _users.value = res.data
                    onResult(Result.success(Unit))
                }
            }
        }
    }
}