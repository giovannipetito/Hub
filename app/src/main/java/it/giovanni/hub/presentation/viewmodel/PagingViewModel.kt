package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.domain.model.Character
import it.giovanni.hub.domain.CharacterPagingSource
import it.giovanni.hub.domain.repository.remote.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingViewModel @Inject constructor(
    private val repository: UsersRepository
) : ViewModel() {

    private val _uiEvents = MutableSharedFlow<UIEvent>()
    val uiEvents: SharedFlow<UIEvent> = _uiEvents.asSharedFlow()

    /**
     * Get data with Paging
     */
    private val pager: Pager<Int, Character> by lazy {
        Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                CharacterPagingSource(repository = repository) { event ->
                    viewModelScope.launch {
                        _uiEvents.emit(event)
                    }
                }
            }
        )
    }

    val charactersFlow: Flow<PagingData<Character>> = pager.flow.cachedIn(viewModelScope)
}

sealed class UIEvent {
    data class ShowSuccess(val message: String) : UIEvent()
    data class ShowError(val message: String) : UIEvent()
}