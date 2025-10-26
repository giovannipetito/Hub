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