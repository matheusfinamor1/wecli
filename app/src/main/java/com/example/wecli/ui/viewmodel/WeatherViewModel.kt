package com.example.wecli.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wecli.extensions.toUiState
import com.example.wecli.repository.weatherRepository.WeatherRepositoryImpl
import com.example.wecli.ui.state.WeatherUiState
import com.example.wecli.useCase.GetLocationUserUseCase
import com.example.wecli.useCase.GetMomentDayUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getLocationUserUseCase: GetLocationUserUseCase,
    private val getMomentDayUseCase: GetMomentDayUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _momentDay = MutableStateFlow("")
    val momentDay: StateFlow<String> = _momentDay.asStateFlow()

    init {
        getMomentDay()
    }

    fun getLocation(lon: Double, lat: Double) {
        viewModelScope.launch {
            getLocationUserUseCase.invoke(lon, lat).collect{
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