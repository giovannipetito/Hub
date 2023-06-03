package it.giovanni.hub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.Data
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

    private val _users: MutableStateFlow<List<Data>> = MutableStateFlow(emptyList<Data>())
    val users: StateFlow<List<Data>>
        get() = _users

    init {
        fetchUsers(1)
    }

    fun fetchUsers(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result: HubResult<UsersResponse> = usersDataSource.getUsers(page)) {
                is HubResult.Success<UsersResponse> -> {
                    if (result.data.data != null) {
                        _users.value = result.data.data!!
                    }
                }
                is HubResult.Error -> {
                    // todo: show error message
                }
            }
        }
    }
}
