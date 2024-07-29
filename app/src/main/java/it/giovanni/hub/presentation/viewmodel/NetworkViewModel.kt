package it.giovanni.hub.presentation.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.datasource.remote.NetworkDataSource
import it.giovanni.hub.data.request.NetworkRequest
import it.giovanni.hub.data.response.NetworkResponse
import it.giovanni.hub.domain.NetworkWorker
import it.giovanni.hub.domain.result.simple.HubResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val dataSource: NetworkDataSource
) : ViewModel() {

    private val _username: MutableState<String> = mutableStateOf("")
    val username: State<String> = _username

    private val _message: MutableState<String> = mutableStateOf("")
    val message: State<String> = _message

    private val _responseMessage: MutableState<String> = mutableStateOf("")
    val responseMessage: State<String> = _responseMessage

    private val _showRationale = MutableStateFlow(false)
    val showRationale: StateFlow<Boolean> = _showRationale.asStateFlow()

    private val _permissionDenied = MutableStateFlow(false)
    val permissionDenied: StateFlow<Boolean> = _permissionDenied.asStateFlow()

    private val _requestStatus: MutableState<String> = mutableStateOf("")
    val requestStatus: State<String> = _requestStatus

    fun setUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun setMessage(newMessage: String) {
        _message.value = newMessage
    }

    fun sendMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = NetworkRequest(username = _username.value, message = _message.value)
            when (val result: HubResult<NetworkResponse> = dataSource.sendMessage(request)) {
                is HubResult.Success<NetworkResponse> -> {
                    _responseMessage.value = result.data.reply
                }
                is HubResult.Error -> {
                    _responseMessage.value = "Error: ${result.message}"
                }
            }
        }
    }

    @SuppressLint("IdleBatteryChargingConstraints")
    fun sendMessage(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

                val inputData = workDataOf("username" to _username.value, "message" to _message.value)

                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresStorageNotLow(false)
                    .setRequiresBatteryNotLow(false)
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .build()

                val workRequest = OneTimeWorkRequestBuilder<NetworkWorker>()
                    // .setConstraints(constraints)
                    .setInputData(inputData)
                    .build()

                val workManager: WorkManager = WorkManager.getInstance(context)
                workManager.enqueue(workRequest)

                workManager.getWorkInfoByIdLiveData(workRequest.id).observe(context as LifecycleOwner) { workInfo ->
                    if (workInfo != null) {
                        when (workInfo.state) {
                            WorkInfo.State.SUCCEEDED -> {
                                val response = workInfo.outputData.getString("success") ?: "No success"
                                _responseMessage.value = response
                                _requestStatus.value = "Success"
                            }
                            WorkInfo.State.ENQUEUED -> {
                                _requestStatus.value = "Enqueued"
                            }
                            WorkInfo.State.FAILED -> {
                                val exception = workInfo.outputData.getString("failure") ?: "No failure"
                                _requestStatus.value = "Failed: $exception"
                            }
                            WorkInfo.State.RUNNING -> {
                                _requestStatus.value = "Running"
                            }
                            WorkInfo.State.BLOCKED -> {
                                _requestStatus.value = "Blocked"
                            }
                            WorkInfo.State.CANCELLED -> {
                                _requestStatus.value = "Cancelled"
                            }
                        }
                    }
                }

                /*
                Or:
                workManager.getWorkInfoByIdLiveData(workRequest.id).observeForever { workInfo ->
                    if (workInfo != null && workInfo.state.isFinished) {
                        val response = workInfo.outputData.getString("reply") ?: "No reply"
                        _responseMessage.value = response
                        _requestStatus.value = "Success"
                    }
                }
                */
            }
        }
    }

    fun setShowRationale(show: Boolean) {
        _showRationale.update { show }
    }

    fun setPermissionDenied(denied: Boolean) {
        _permissionDenied.update { denied }
    }
}