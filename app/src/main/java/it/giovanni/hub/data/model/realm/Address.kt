package it.giovanni.hub.data.model.realm

import io.realm.kotlin.types.EmbeddedRealmObject

/**
 * 1 Teacher to 1 Address
 * 1 Teacher to many Course
 * many Student to many Course
 */

class Address: EmbeddedRealmObject {
    var fullName: String = ""
    var street: String = ""
    var houseNumber: Int = 0
    var zip: Int = 0
    var city: String = ""
    var teacher: Teacher? = null
}