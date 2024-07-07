package com.example.wecli

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {
    private val _splashShowValue = MutableStateFlow(true)
    val splashShowValue: StateFlow<Boolean> = _splashShowValue.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            _splashShowValue.value = false
        }
    }

}