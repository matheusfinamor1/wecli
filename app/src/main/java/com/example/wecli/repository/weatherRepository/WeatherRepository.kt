package com.example.wecli.repository.weatherRepository

import com.example.wecli.data.weatherCurrentData.WeatherResponse
import com.example.wecli.response.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun fetchWeather(lon: Double, lat: Double): Flow<Resource<WeatherResponse>>
}