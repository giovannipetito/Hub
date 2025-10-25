package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.R
import it.giovanni.hub.domain.model.Person
import it.giovanni.hub.domain.AlertBarState
import it.giovanni.hub.domain.StringManager
import it.giovanni.hub.domain.error.returnErrorMessage
import it.giovanni.hub.domain.repositoryint.remote.AuthRepository
import it.giovanni.hub.domain.usecase.PasswordValidatorImpl
import it.giovanni.hub.domain.result.pro.HubResultPro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ErrorHandlingViewModel @Inject constructor(
    private val passwordValidator: PasswordValidatorImpl,
    private val repository: AuthRepository
): ViewModel() {
    private val _passwordError: MutableState<StringManager> = mutableStateOf(value = StringManager.ResourceString(R.string.empty_error))
    var passwordError: State<StringManager> = _passwordError

    private val _person: MutableStateFlow<Person> = MutableStateFlow(Person(id = -1, firstName = "", lastName = "", visibility = false))
    val person: StateFlow<Person> = _person.asStateFlow()
    /*
    Oppure:
    val person: StateFlow<Person>
        get() = _person
    */

    fun checkAndRegisterPassword(password: String, state: AlertBarState) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = passwordValidator.validatePassword(password)) {
                is HubResultPro.Error -> {
                    _passwordError.value = result.error.returnErrorMessage()
                }
                is HubResultPro.Success -> {
                    _passwordError.value = StringManager.ResourceString(R.string.empty_error)
                    viewModelScope.launch {
                        when (val response = repository.register(password)) {
                            is HubResultPro.Error -> {
                                state.addError(stringManager = response.error.returnErrorMessage())
                            }
                            is HubResultPro.Success -> {
                                _person.value = response.data
                                state.addSuccess(success = "Person uploaded successfully: " + _person.value.firstName + " " + _person.value.lastName)
                            }
                        }
                    }
                }
            }
        }
    }
}