package com.example.desibel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState


    fun sendValue(){
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Success(NoiseCancel().noiseLevel)
        }
    }

    sealed class UiState {
        data class Success(val value: Double) : UiState()
        object Loading : UiState()
    }
}