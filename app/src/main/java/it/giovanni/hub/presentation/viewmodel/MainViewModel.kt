package it.giovanni.hub.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.R
import it.giovanni.hub.data.datasource.local.DataStoreRepository
import it.giovanni.hub.data.model.SignedInUser
import it.giovanni.hub.data.response.SignInResponse
import it.giovanni.hub.domain.service.CounterService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    private val _keepSplashOpened: MutableState<Boolean> = mutableStateOf(false)
    var keepSplashOpened: State<Boolean> = _keepSplashOpened

    private val _firstAccess: MutableState<Boolean> = mutableStateOf(false)
    var firstAccess: State<Boolean> = _firstAccess

    private val auth = Firebase.auth

    var isLoading: MutableState<Boolean> = mutableStateOf(false)
        private set

    private val _signInResponse: MutableStateFlow<SignInResponse> = MutableStateFlow(SignInResponse(user = null, errorMessage = ""))
    val signInResponse: StateFlow<SignInResponse> = _signInResponse.asStateFlow()

    private val _counterService: MutableState<CounterService> = mutableStateOf(CounterService())
    var counterService: State<CounterService> = _counterService

    fun setSplashOpened(state: Boolean) {
        _keepSplashOpened.value = state
    }

    fun setFirstAccess(firstAccess: Boolean) {
        _firstAccess.value = firstAccess
    }

    fun saveLoginState(state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLoginState(state = state)
        }
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun setCounterService(counterService: CounterService) {
        _counterService.value = counterService
    }

    suspend fun signIn(context: Context, credentialManager: CredentialManager) {
        try {
            val result = credentialManager.getCredential(context, buildSignInRequest(context))
            val credential: Credential = result.credential
            val googleIdToken: String = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val authCredential: AuthCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

            val user: FirebaseUser? = auth.signInWithCredential(authCredential).await().user
            _signInResponse.value = SignInResponse(
                user = user?.run {
                    SignedInUser(
                        uid = uid,
                        displayName = displayName,
                        email = email,
                        photoUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
            setLoading(false)
        } catch (e: Exception) {
            setLoading(false)
            e.printStackTrace()
            if (e is CancellationException) throw e

            _signInResponse.value = SignInResponse(
                user = null,
                errorMessage = e.message
            )
        }
    }

    private fun buildSignInRequest(context: Context): GetCredentialRequest {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return request
    }

    suspend fun signOut(credentialManager: CredentialManager) {
        try {
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException)
                throw e
        }
    }

    fun getSignedInUser(): SignedInUser? = auth.currentUser?.run {
        SignedInUser(
            uid = uid,
            displayName = displayName,
            email = email,
            photoUrl = photoUrl?.toString()
        )
    }

    fun resetState() {
        _signInResponse.update { SignInResponse(null, null)  }
    }
}