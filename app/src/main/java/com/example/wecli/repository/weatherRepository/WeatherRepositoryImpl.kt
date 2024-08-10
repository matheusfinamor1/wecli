package com.example.wecli.repository.weatherRepository

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
            weatherService.getWeatherImage(weather.weather[0].icon)
            emit(Resource.Success(weather))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}