package it.giovanni.hub.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HairColorViewModel @Inject constructor(
    // @ApplicationContext private val context: Context
) : ViewModel() {

    var imageUrl by mutableStateOf<String?>(null)
        private set
}