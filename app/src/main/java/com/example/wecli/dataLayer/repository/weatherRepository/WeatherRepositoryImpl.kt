package com.example.wecli.dataLayer.repository.weatherRepository

import com.example.wecli.dataLayer.data.weatherCurrentData.WeatherResponse
import com.example.wecli.dataLayer.response.Resource
import com.example.wecli.dataLayer.service.weatherCurrentService.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(
    private val weatherService: WeatherService
) : WeatherRepository {
    override fun fetchWeather(lat: Double, lon: Double): Flow<Resource<WeatherResponse>> = flow {
        emit(Resource.Loading())
        try {
            val weather = weatherService.fetchWeather(lat, lon)
            emit(Resource.Success(weather))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}