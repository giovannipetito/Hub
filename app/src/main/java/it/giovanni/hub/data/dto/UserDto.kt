package it.giovanni.hub.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Definizione:
 * Il DTO (Data Transfer Object) è un semplice container di dati del data layer utilizzato per
 * trasferire i dati oltre i confini di process/layer (rete, database) senza alcun comportamento.
 *
 * Caratteristiche principali:
 * - Solo struttura, nessuna business logic (solo campi + valori predefiniti null/empty).
 * - Serialization-friendly (facile da convertire da/a JSON).
 * - Modellato in base alla sorgente (response API), non in base al domain.
 *
 * Benefici del DTO:
 * - Disaccoppia l'app da schemi esterni (API/DB); il DTO può cambiare quando cambia l’API senza
 *   modificare il domain.
 * - I modelli del domain possono evolvere indipendentemente dalle modifiche alle API.
 * - Sicurezza/validazione: elimina o sanifica i campi non attendibili prima che entrino nel domain.
 *
 * Clean Architecture:
 * - Il DTO risie nel data layer (ad esempio, remote/dto per le response di Retrofit).
 * - Si mappa DTO → Domain nel data layer; il domain layer non vede mai i DTO.
 * - Il mapper riduce il coupling (accoppiamento) tra layer e semplifica i test.
 * - Non si espone al domain/presentation
 *
 * DTO - Domain Model - Entity:
 * - DTO: rispecchia la forma esterna; utilizzato per il trasferimento/serializzazione.
 * - Domain Model: esprime la business logic; utilizzato da use cases.
 * - Entity (locale): forma di persistenza del DB (ad esempio, Room @Entity).
 */
data class UserDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("avatar")
    val avatar: String
)