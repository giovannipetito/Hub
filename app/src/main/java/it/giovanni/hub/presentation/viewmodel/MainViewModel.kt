package it.giovanni.hub.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.R
import it.giovanni.hub.data.repository.local.DataStoreRepository
import it.giovanni.hub.domain.model.SignedInUser
import it.giovanni.hub.data.dto.SignInResponse
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

    private val tag = MainViewModel::class.java.simpleName

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
        setLoading(true)

        try {
            // 1) Try with previously-authorized accounts first
            val result = try {
                credentialManager.getCredential(
                    context = context,
                    request = buildSignInRequest(context, filterAuthorizedAccounts = true)
                )
            } catch (e: NoCredentialException) {
                // 2) Fallback: allow any Google account on device (first login / new account)
                Log.i(tag, "NoCredentialException: $e")
                credentialManager.getCredential(
                    context = context,
                    request = buildSignInRequest(context, filterAuthorizedAccounts = false)
                )
            }

            val credential: Credential = result.credential

            // IMPORTANT: Only parse if it's a Google ID token CustomCredential
            val googleIdToken = when {
                credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                    GoogleIdTokenCredential.createFrom(credential.data).idToken
                }
                else -> {
                    throw IllegalStateException("Credential is not a Google ID token credential.")
                }
            }

            val firebaseCredential: AuthCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

            val user: FirebaseUser? = auth.signInWithCredential(firebaseCredential).await().user

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
        } catch (e: GetCredentialCancellationException) {
            // User closed the sheet / canceled: not really an "error"
            Log.i(tag, "GetCredentialCancellationException: $e")
            _signInResponse.value = SignInResponse(user = null, errorMessage = "Cancelled")
        } catch (e: GetCredentialException) {
            _signInResponse.value = SignInResponse(user = null, errorMessage = e.message)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            _signInResponse.value = SignInResponse(user = null, errorMessage = e.message)
        } finally {
            setLoading(false)
        }
    }

    private fun buildSignInRequest(
        context: Context,
        filterAuthorizedAccounts: Boolean
    ): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            // Use the SERVER/WEB client id (generated by google-services.json)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(filterAuthorizedAccounts)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    suspend fun signOut(credentialManager: CredentialManager) {
        try {
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
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