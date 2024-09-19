package com.example.wecli.uiLayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wecli.domainLayer.useCase.combinedWeatherUseCase.GetCombinedWeatherUseCase
import com.example.wecli.domainLayer.useCase.momentDayUseCase.GetMomentDayUseCase
import com.example.wecli.uiLayer.ui.state.WeatherUiState
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

    private val _listFilterDays = MutableStateFlow(WeatherUiState())
    val listFilterDays: StateFlow<WeatherUiState> = _listFilterDays.asStateFlow()

    init {
        getMomentDay()
    }

    fun getCombinedWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            getCombinedWeatherUseCase.invoke(lat, lon).collect {
                if (_listFilterDays.value.forecastList?.size == 0) {
                    _uiState.value = it.data ?: WeatherUiState()
                    _listFilterDays.value = it.data ?: WeatherUiState()
                } else {
                    _listFilterDays.value = uiState.value.copy(
                        isLoading = false,
                        forecastList = _listFilterDays.value.forecastList
                    )
                }
            }
        }
    }

    fun filterDayForecast(day: String) {
        viewModelScope.launch {
            _listFilterDays.value = uiState.value.copy(
                isLoading = true,
                forecastList = uiState.value.forecastList
            )
            try {
                if (day == "Todos") {
                    val allData = uiState.value.forecastList
                    _listFilterDays.value = uiState.value.copy(
                        isLoading = false,
                        forecastList = allData
                    )
                } else {
                    val filteredForecastList = uiState.value.forecastList?.filter {
                        it.dataForecastUiState == day
                    } ?: emptyList()
                    _listFilterDays.value = uiState.value.copy(
                        isLoading = false,
                        forecastList = filteredForecastList
                    )
                }
            } catch (e: Exception) {
                _listFilterDays.value = uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
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