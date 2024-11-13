package com.matheusfinamor.wecli.dataLayer.service.forecastService

import com.matheusfinamor.wecli.dataLayer.data.forecastData.ForecastResponse

interface ForecastService {
    suspend fun fetchForecast(lat: Double, lon: Double): ForecastResponse
}