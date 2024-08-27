package com.example.wecli.dataLayer.service.weatherCurrentService

import com.example.wecli.dataLayer.data.weatherCurrentData.WeatherResponse

interface WeatherService {
    suspend fun fetchWeather(lat: Double, lon: Double): WeatherResponse
}