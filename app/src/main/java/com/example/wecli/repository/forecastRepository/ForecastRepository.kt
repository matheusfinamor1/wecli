package com.example.wecli.repository.forecastRepository

import com.example.wecli.data.forecastData.ForecastResponse
import com.example.wecli.response.Resource
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    fun fetchForecast(lat: Double, lon: Double): Flow<Resource<ForecastResponse>>
}