package com.example.wecli.dataLayer.repository.forecastRepository

import com.example.wecli.dataLayer.data.forecastData.ForecastResponse
import com.example.wecli.dataLayer.response.Resource
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    fun fetchForecast(lat: Double, lon: Double): Flow<Resource<ForecastResponse>>
}