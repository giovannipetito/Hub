package it.giovanni.hub.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.giovanni.hub.data.repositoryimpl.local.DataStoreRepository
import it.giovanni.hub.navigation.routes.BottomBarRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    private val _startDestination: MutableState<String> = mutableStateOf(BottomBarRoutes.Home.route)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
            repository.getLoginState().collect { completed ->
                if (completed) {
                    _startDestination.value = BottomBarRoutes.Home.route
                } else {
                    _startDestination.value = "Wizard"
                }
            }
        }
    }

    @Composable
    fun KeepOrientationPortrait() {
        val context = LocalContext.current
        val orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        DisposableEffect(orientation) {
            val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
            val originalOrientation = activity.requestedOrientation
            activity.requestedOrientation = orientation
            onDispose {
                // Restore the original orientation when the view disappears.
                activity.requestedOrientation = originalOrientation
            }
        }
    }

    private fun Context.findActivity(): Activity? = when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}