package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.domain.result.simple.HubResult
import it.giovanni.hub.domain.usecase.GetCoroutinesUsersUseCase
import it.giovanni.hub.domain.usecase.SearchParams
import it.giovanni.hub.domain.usecase.SearchCoroutinesUsersUseCase
import it.giovanni.hub.domain.usecase.SortBy
import it.giovanni.hub.presentation.mapper.toPresentation
import it.giovanni.hub.presentation.model.UiUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersCoroutinesViewModel @Inject constructor(
    private val getUsersUseCase: GetCoroutinesUsersUseCase,
    private val searchUsersUseCase: SearchCoroutinesUsersUseCase,
) : ViewModel() {

    private val _users: MutableStateFlow<List<UiUser>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<UiUser>>
        get() = _users

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    /**
     * Usa init se vuoi che il fetch sia sempre eseguito quando il VM nasce
     * (indipendente dalla UI); in tal caso il LaunchedEffect diventa inutile
     */
    // init {
    //     fetchCoroutinesUsers(page = 1) {}
    // }

    /**
     * Get data with Coroutines
     */
    fun fetchCoroutinesUsers(page: Int, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            when (val result = getUsersUseCase(page)) {
                is HubResult.Success -> {
                    val uiResult = result.data.map { it.toPresentation() }

                    _users.update {
                        uiResult
                    }
                    // oppure: _users.value = uiResult

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
            when (val result = searchUsersUseCase(params)) {
                is HubResult.Error -> onResult(Result.failure(Exception(result.message)))
                is HubResult.Success -> {
                    val uiResult = result.data.map { it.toPresentation() }

                    _users.update {
                        uiResult
                    }
                    // oppure: _users.value = uiResult

                    onResult(Result.success(Unit))
                }
            }
        }
    }
}