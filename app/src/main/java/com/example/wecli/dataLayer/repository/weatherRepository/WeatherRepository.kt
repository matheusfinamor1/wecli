package com.example.wecli.dataLayer.repository.weatherRepository

import com.example.wecli.dataLayer.data.weatherCurrentData.WeatherResponse
import com.example.wecli.dataLayer.response.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherResponse>>
}