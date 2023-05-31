package it.giovanni.hub.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.giovanni.hub.AppModule
import it.giovanni.hub.Data
import it.giovanni.hub.UsersResponse
import kotlinx.coroutines.launch
import it.giovanni.hub.Result

class RecyclerViewViewModel : ViewModel() {

    private val _users: MutableLiveData<List<Data>> = MutableLiveData<List<Data>>()
    val users: LiveData<List<Data>>
        get() = _users

    /*
    private var _users2: List<Data> by mutableStateOf(Data)
    val users2: List<Data>
        get() = _users2
    */

    init {
        fetchUsers(1)
    }

    fun fetchUsers(page: Int) {
        viewModelScope.launch {
            when (val result: Result<UsersResponse> = AppModule.getUsers(page)) {
                is Result.Success<UsersResponse> -> {

                    _users.value = result.data.data
                }
                is Result.Error -> {
                    // todo: show error message
                }
            }
        }
    }
}
