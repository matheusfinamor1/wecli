package com.example.wecli.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wecli.data.WeatherResponse
import com.example.wecli.extensions.toUiState
import com.example.wecli.repository.WeatherRepositoryImpl
import com.example.wecli.ui.state.WeatherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepositoryImpl
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun getLocation(lon: Double, lat: Double) {
        viewModelScope.launch {
            weatherRepository.fetchWeather(lon, lat)
                .catch { exception -> Log.d("Response", "Erro: ${exception.message}") }
                .collect { weather ->
                    val uiState = weather.data?.toUiState() ?: WeatherUiState()
                    _uiState.value = uiState
                }
        }

    }
}