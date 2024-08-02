package com.example.wecli.service

import com.example.wecli.data.WeatherResponse

interface WeatherService {
    suspend fun fetchWeather(lon: Double, lat: Double): WeatherResponse
}