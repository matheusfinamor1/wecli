package com.matheusfinamor.wecli.dataLayer.service.weatherCurrentService

import com.matheusfinamor.wecli.dataLayer.data.weatherCurrentData.WeatherResponse

interface WeatherService {
    suspend fun fetchWeather(lat: Double, lon: Double): WeatherResponse
}