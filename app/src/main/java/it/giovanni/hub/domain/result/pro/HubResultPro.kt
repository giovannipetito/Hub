package it.giovanni.hub.domain.result.pro

import it.giovanni.hub.domain.error.RootError

sealed interface HubResultPro<out D, out E: RootError> {
    data class Success<out D, out E: RootError>(val data: D): HubResultPro<D, E>
    data class Error<out D, out E: RootError>(val error: E): HubResultPro<D, E>
}