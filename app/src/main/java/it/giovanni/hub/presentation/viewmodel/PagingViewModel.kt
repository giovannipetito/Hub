package it.giovanni.hub.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.datasource.remote.DataSource
import it.giovanni.hub.data.model.Character
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.domain.CharacterPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PagingViewModel @Inject constructor(
    private val dataSource: DataSource
) : ViewModel() {

    /**
     * Get data with Paging
     */
    fun getDataFlow(state: AlertBarState): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = {
                CharacterPagingSource(dataSource = dataSource, state = state)
            }
        ).flow
    }
}