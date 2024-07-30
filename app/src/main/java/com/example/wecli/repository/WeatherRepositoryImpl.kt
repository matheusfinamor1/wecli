package com.example.wecli.repository

import com.example.wecli.data.WeatherResponse
import com.example.wecli.response.Resource
import com.example.wecli.service.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl(
    private val weatherService: WeatherService
) : WeatherRepository {
    override fun fetchWeather(lon: Double, lat: Double): Flow<Resource<WeatherResponse>> = flow {
        emit(Resource.Loading())
        try {
            val weather = weatherService.fetchWeather(lon, lat)
            emit(Resource.Success(weather))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

}