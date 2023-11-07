package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.repository.local.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    private val _keepSplashOpened: MutableState<Boolean> = mutableStateOf(false)
    var keepSplashOpened: State<Boolean> = _keepSplashOpened

    fun setSplashOpened(boolean: Boolean) {
        _keepSplashOpened.value = boolean
    }

    fun saveLoginState(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLoginState(state = state)
        }
    }
}