package it.giovanni.hub.domain

import it.giovanni.hub.data.model.Character
import androidx.paging.PagingSource
import androidx.paging.PagingState
import it.giovanni.hub.data.datasource.remote.UsersDataSource
import it.giovanni.hub.data.response.CharactersResponse
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class CharacterPagingSource(
    private val dataSource: UsersDataSource,
    private val state: AlertBarState
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val currentPage = params.key ?: 1
        return try {
            val response: CharactersResponse = dataSource.getCharacters(currentPage)

            // val mutableListOfCharacters = mutableListOf<Character>()
            // mutableListOfCharacters.addAll(response.results)

            if (response.results.isNotEmpty()) {
                state.addSuccess(success = "Loading successful!")
                LoadResult.Page(
                    data = response.results, // mutableListOfCharacters
                    prevKey = if (currentPage == 1) null else currentPage.minus(1),
                    nextKey = if (response.results.isEmpty()) null else currentPage.plus(1)
                )
            } else {
                state.addError(Exception("No items found."))
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection: ")
            state.addError(Exception(error.message + exception.localizedMessage))
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            state.addError(Exception(exception.localizedMessage))
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }
}