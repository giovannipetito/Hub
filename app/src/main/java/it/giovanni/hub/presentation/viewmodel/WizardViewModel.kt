package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.repository.local.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WizardViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    fun saveWizardState(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveWizardState(state = state)
        }
    }
}