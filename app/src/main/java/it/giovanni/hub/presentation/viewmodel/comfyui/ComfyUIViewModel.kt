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

    private val _baseUrl = MutableStateFlow("")
    val baseUrl: StateFlow<String> = _baseUrl

    init {
        viewModelScope.launch {
            repository.getComfyUIBaseUrl().collect { savedUrl ->
                if (savedUrl != null) {
                    _baseUrl.value = savedUrl
                }
            }
        }
    }

    fun setBaseUrl(baseUrl: String) = viewModelScope.launch {
        repository.saveComfyUIBaseUrl(baseUrl)
        _baseUrl.value = baseUrl
    }
}