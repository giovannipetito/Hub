package it.giovanni.hub.domain

import it.giovanni.hub.domain.model.Character
import androidx.paging.PagingSource
import androidx.paging.PagingState
import it.giovanni.hub.data.dto.CharactersResponse
import it.giovanni.hub.domain.repositoryint.remote.UsersRepository
import it.giovanni.hub.presentation.viewmodel.UIEvent
import retrofit2.HttpException
import java.io.IOException

class CharacterPagingSource(
    private val repository: UsersRepository,
    private val onEvent: (UIEvent) -> Unit
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val currentPage = params.key ?: 1
        return try {
            val response: CharactersResponse = repository.getCharacters(currentPage)

            if (response.results.isNotEmpty()) {
                if (currentPage == 1)
                    onEvent(UIEvent.ShowSuccess("Loading successful!"))
            } else {
                onEvent(UIEvent.ShowError("No items found!"))
            }

            LoadResult.Page(
                data = response.results,
                prevKey = if (currentPage == 1) null else currentPage.minus(1),
                nextKey = if (response.results.isEmpty()) null else currentPage.plus(1)
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection: ")
            onEvent(UIEvent.ShowError("" + exception))
            LoadResult.Error(exception) // error
        } catch (exception: HttpException) {
            onEvent(UIEvent.ShowError(exception.localizedMessage ?: "Unknown HTTP error"))
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }
}