package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.Graph
import it.giovanni.hub.data.repository.local.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val repository: DataStoreRepository,
) : ViewModel() {

    private val _startDestination: MutableState<String> = mutableStateOf(Graph.MAIN_ROUTE)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            repository.getLoginState().collect { completed ->
                if (completed) {
                    _startDestination.value = Graph.MAIN_ROUTE
                } else {
                    _startDestination.value = Graph.WIZARD_ROUTE
                }
            }
        }
    }
}