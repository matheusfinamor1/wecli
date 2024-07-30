package com.example.wecli.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wecli.data.WeatherResponse
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
                    val uiState = mapToUiState(weather.data)
                    _uiState.value = uiState
                }
        }

    }

    private fun mapToUiState(weather: WeatherResponse?): WeatherUiState {
        return weather?.let {
            WeatherUiState(
                main = it.weather[0].main,
                description = it.weather[0].description,
                base = it.base,
                temp = it.main.temp,
                feelsLike = it.main.feels_like,
                tempMin = it.main.temp_min,
                tempMax = it.main.temp_max,
                pressure = it.main.pressure,
                humidity = it.main.humidity,
                visibility = it.visibility,
                windSpeed = it.wind.speed,
                cloudsAll = it.clouds.all,
                country = it.sys.country,
                name = it.name
            )
        } ?: WeatherUiState()
    }
}