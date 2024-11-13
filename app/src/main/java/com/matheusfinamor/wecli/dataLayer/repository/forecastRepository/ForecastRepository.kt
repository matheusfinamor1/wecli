package com.matheusfinamor.wecli.dataLayer.repository.forecastRepository

import com.matheusfinamor.wecli.dataLayer.data.forecastData.ForecastResponse
import com.matheusfinamor.wecli.dataLayer.response.Resource
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    fun fetchForecast(lat: Double, lon: Double): Flow<Resource<ForecastResponse>>
}