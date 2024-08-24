package com.example.wecli.service.forecastService

import com.example.wecli.data.forecastData.ForecastResponse

interface ForecastService {
    suspend fun fetchForecast(lat: Double, lon: Double): ForecastResponse
}