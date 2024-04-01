package it.giovanni.hub.data.datasource.remote

import it.giovanni.hub.data.model.Person
import it.giovanni.hub.domain.error.Error
import it.giovanni.hub.domain.result.pro.HubResultPro

interface AuthDataSource {
    suspend fun register(password: String): HubResultPro<Person, Error>
}