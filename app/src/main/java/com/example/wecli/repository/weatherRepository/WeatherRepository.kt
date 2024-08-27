package com.example.wecli.repository.weatherRepository

import com.example.wecli.data.weatherCurrentData.WeatherResponse
import com.example.wecli.response.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherResponse>>
}