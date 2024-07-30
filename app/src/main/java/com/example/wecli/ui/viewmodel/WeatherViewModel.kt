package com.example.wecli.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wecli.data.WeatherResponse
import com.example.wecli.repository.WeatherRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepositoryImpl
) : ViewModel() {
    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather.asStateFlow()

    fun getLocation(lon: Double, lat: Double) {
        viewModelScope.launch {
            weatherRepository.fetchWeather(lon,lat)
                .catch { exception -> Log.d("Response", "Erro: ${exception.message}") }
                .collect { weather ->
                    _weather.value = weather.data
                    Log.d("Response", "Sucesso: ${weather.data?.name}")
                }
        }

    }
}