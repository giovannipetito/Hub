package it.giovanni.hub.data.repositoryimpl.remote

import it.giovanni.hub.domain.error.Network
import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.domain.error.Error
import it.giovanni.hub.domain.repositoryint.remote.AuthRepository
import it.giovanni.hub.domain.result.pro.HubResultPro
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(): AuthRepository {

    override suspend fun register(password: String): HubResultPro<Person, Error> {
        // API call logic
        return try {
            val user = Person(1, "Giovanni", "Petito", true)
            HubResultPro.Success(user)
        } catch (e: HttpException) {
            when (e.code()) {
                408 -> HubResultPro.Error(Network.REQUEST_TIMEOUT)
                413 -> HubResultPro.Error(Network.PAYLOAD_ERROR)
                else -> HubResultPro.Error(Network.UNKNOWN_ERROR)
            }
        }
    }
}