package com.example.wecli.service.weatherCurrentService

import com.example.wecli.data.weatherCurrentData.WeatherResponse

interface WeatherService {
    suspend fun fetchWeather(lon: Double, lat: Double): WeatherResponse
}