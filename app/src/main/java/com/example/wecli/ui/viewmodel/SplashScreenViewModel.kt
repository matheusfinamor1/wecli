package com.example.wecli.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {
    private val _splashShowValue = MutableStateFlow(true)
    val splashShowValue: StateFlow<Boolean> = _splashShowValue.asStateFlow()

    init {
        viewModelScope.launch {
            _splashShowValue.value = true
        }
    }

    fun updateSplashShowValue(value: Boolean) {
        _splashShowValue.value = value
    }

}