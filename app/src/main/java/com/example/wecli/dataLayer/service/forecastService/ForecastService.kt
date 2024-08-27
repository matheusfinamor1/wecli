package com.example.wecli.dataLayer.service.forecastService

import com.example.wecli.dataLayer.data.forecastData.ForecastResponse

interface ForecastService {
    suspend fun fetchForecast(lat: Double, lon: Double): ForecastResponse
}