package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import it.giovanni.hub.Graph
import it.giovanni.hub.data.repository.local.DataStoreRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(Graph.WIZARD_ROUTE)
    val startDestination: State<String> = _startDestination

    init {
        /*
        viewModelScope.launch {
            delay(3000)
            repository.getWizardState().collect { completed ->
                Log.i("[SPLASH]", "completed: $completed")
                if (completed) {
                    _startDestination.value = Graph.LOADING_ROUTE
                } else {
                    _startDestination.value = Graph.WIZARD_ROUTE
                }
            }
            _isLoading.value = false
        }
        */

        runBlocking {
            delay(3000)
            val completed: Boolean = repository.getWizardState().first()
            if (completed) {
                _startDestination.value = Graph.LOADING_ROUTE
            } else {
                _startDestination.value = Graph.WIZARD_ROUTE
            }
        }
    }
}