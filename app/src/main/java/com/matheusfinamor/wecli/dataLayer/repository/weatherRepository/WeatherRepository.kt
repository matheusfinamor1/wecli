package com.matheusfinamor.wecli.dataLayer.repository.weatherRepository

import com.matheusfinamor.wecli.dataLayer.data.weatherCurrentData.WeatherResponse
import com.matheusfinamor.wecli.dataLayer.response.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherResponse>>
}