package com.example.wecli.useCase.weatherUseCase

import com.example.wecli.data.weatherCurrentData.WeatherResponse
import com.example.wecli.repository.weatherRepository.WeatherRepositoryImpl
import com.example.wecli.response.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWeatherUserUseCase(
    private val weatherRepository: WeatherRepositoryImpl
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Resource<WeatherResponse?>> = flow {
        emit(Resource.Loading())
        weatherRepository.fetchWeather(lat, lon).collect { weatherResource ->
            when (weatherResource) {
                is Resource.Success -> emit(Resource.Success(weatherResource.data))
                is Resource.Error -> emit(Resource.Error(weatherResource.message.toString()))
                is Resource.Loading -> emit(Resource.Loading())
            }
        }
    }
}