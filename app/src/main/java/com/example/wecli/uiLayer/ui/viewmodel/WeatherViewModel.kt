package com.example.wecli.uiLayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wecli.uiLayer.ui.state.WeatherUiState
import com.example.wecli.domainLayer.useCase.combinedWeatherUseCase.GetCombinedWeatherUseCase
import com.example.wecli.domainLayer.useCase.momentDayUseCase.GetMomentDayUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getMomentDayUseCase: GetMomentDayUseCase,
    private val getCombinedWeatherUseCase: GetCombinedWeatherUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _momentDay = MutableStateFlow("")
    val momentDay: StateFlow<String> = _momentDay.asStateFlow()

    init {
        getMomentDay()
    }

    fun getCombinedWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            getCombinedWeatherUseCase.invoke(lat, lon).collect {
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