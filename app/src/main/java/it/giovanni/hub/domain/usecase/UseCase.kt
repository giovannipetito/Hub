package it.giovanni.hub.domain.usecase

import io.reactivex.rxjava3.core.Single
import it.giovanni.hub.domain.model.User
import it.giovanni.hub.domain.repositoryint.remote.UsersRepository
import it.giovanni.hub.domain.result.simple.HubResult
import javax.inject.Inject

// Using operator fun invoke() you can call the use case like a function.

class GetCoroutinesUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(page: Int): HubResult<List<User>> {
        return repository.getCoroutinesUsers(page)
    }
}

// Don't wrap Rx errors into HubResult
/*
class GetRxJavaUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    operator fun invoke(page: Int): Single<List<User>> {
        return repository.getRxJavaUsers(page)
    }
}
*/

// Wrap Rx errors into HubResult so the VM always handles a single shape
class GetRxJavaUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    operator fun invoke(page: Int): Single<HubResult<List<User>>> {
        return repository.getRxJavaUsers(page)
            .map<HubResult<List<User>>> { list -> HubResult.Success(list) }
            .onErrorReturn { throwable -> HubResult.Error(throwable.message) }
    }
}

class SearchCoroutinesUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(params: SearchParams): HubResult<List<User>> {
        return when (val result = repository.getCoroutinesUsers(params.page)) {
            is HubResult.Error -> result
            is HubResult.Success -> {
                val filteredUsers = result.data.filter { user ->
                    if (params.query.isBlank()) return@filter true
                    val query = params.query.trim().lowercase()
                    user.firstName.lowercase().contains(query) ||
                            user.lastName.lowercase().contains(query)  ||
                            user.email.lowercase().contains(query)
                }

                val sortedUsers = when (params.sortBy) {
                    SortBy.FIRST_NAME -> filteredUsers.sortedBy { it.firstName.lowercase() }
                    SortBy.LAST_NAME  -> filteredUsers.sortedBy { it.lastName.lowercase() }
                    SortBy.EMAIL      -> filteredUsers.sortedBy { it.email.lowercase() }
                }.let { list -> if (params.ascending) list else list.asReversed() }

                HubResult.Success(sortedUsers)
            }
        }
    }
}

class SearchRxJavaUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    operator fun invoke(params: SearchParams): Single<HubResult<List<User>>> {
        val query = params.query.trim().lowercase()

        return repository.getRxJavaUsers(params.page) // Single<List<User>>
            .map { users ->
                val filteredUsers = if (query.isEmpty()) {
                    users
                } else {
                    users.filter { user ->
                        user.firstName.lowercase().contains(query) ||
                                user.lastName.lowercase().contains(query) ||
                                user.email.lowercase().contains(query)
                    }
                }

                val sortedUsers = when (params.sortBy) {
                    SortBy.FIRST_NAME -> filteredUsers.sortedBy { it.firstName.lowercase() }
                    SortBy.LAST_NAME  -> filteredUsers.sortedBy { it.lastName.lowercase() }
                    SortBy.EMAIL      -> filteredUsers.sortedBy { it.email.lowercase() }
                }.let { list -> if (params.ascending) list else list.asReversed() }

                HubResult.Success(sortedUsers) as HubResult<List<User>>
            }
            .onErrorReturn { t -> HubResult.Error(t.message) }
    }
}

data class SearchParams(
    val page: Int,
    val query: String = "",
    val sortBy: SortBy = SortBy.LAST_NAME,
    val ascending: Boolean = true
)

enum class SortBy { FIRST_NAME, LAST_NAME, EMAIL }