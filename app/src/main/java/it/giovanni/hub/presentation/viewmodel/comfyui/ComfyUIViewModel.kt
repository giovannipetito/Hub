package it.giovanni.hub.presentation.viewmodel.comfyui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.repositoryimpl.local.DataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComfyUIViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    private val _comfyUrl = MutableStateFlow("")
    val comfyUrl: StateFlow<String> = _comfyUrl

    init {
        viewModelScope.launch {
            repository.getComfyUrl().collect { savedUrl ->
                if (savedUrl != null) {
                    _comfyUrl.value = savedUrl
                }
            }
        }
    }

    fun setBaseUrl(baseUrl: String) = viewModelScope.launch {
        repository.saveComfyUrl(baseUrl)
        _comfyUrl.value = baseUrl
    }
}