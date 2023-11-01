package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.datasource.remote.DataSource
import it.giovanni.hub.data.model.Character
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.domain.usecase.CharacterPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataSource: DataSource,
    private val repository: DataStoreRepository
): ViewModel() {

    /**
     * Get data with Paging
     */
    fun getDataFlow(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                CharacterPagingSource(dataSource)
            }
        ).flow
    }

    fun saveLoginState(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLoginState(state = state)
        }
    }
}