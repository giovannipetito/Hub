package it.giovanni.hub.domain.repository.remote

import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.domain.error.Error
import it.giovanni.hub.domain.result.pro.HubResultPro

interface AuthRepository {
    suspend fun register(password: String): HubResultPro<Person, Error>
}