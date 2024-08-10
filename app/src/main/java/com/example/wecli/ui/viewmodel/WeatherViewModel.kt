package com.example.wecli.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wecli.ui.state.WeatherUiState
import com.example.wecli.useCase.GetWeatherUserUseCase
import com.example.wecli.useCase.GetMomentDayUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getWeatherUserUseCase: GetWeatherUserUseCase,
    private val getMomentDayUseCase: GetMomentDayUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _momentDay = MutableStateFlow("")
    val momentDay: StateFlow<String> = _momentDay.asStateFlow()

    init {
        getMomentDay()
    }

    fun getWeatherUser(lon: Double, lat: Double) {
        viewModelScope.launch {
            getWeatherUserUseCase.invoke(lon, lat).collect{
                _uiState.value = it.data ?: WeatherUiState()
            }
        }
    }

    private fun getMomentDay() {
        viewModelScope.launch {
            getMomentDayUseCase.invoke()
                .collect {
                    _momentDay.value = it
                }
        }
    }
}