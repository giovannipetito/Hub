package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.repository.local.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    fun saveLoginState(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLoginState(state = state)
        }
    }
}