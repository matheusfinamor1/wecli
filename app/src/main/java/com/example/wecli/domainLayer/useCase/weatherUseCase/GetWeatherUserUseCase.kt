package com.example.wecli.domainLayer.useCase.weatherUseCase

import com.example.wecli.dataLayer.data.weatherCurrentData.WeatherResponse
import com.example.wecli.dataLayer.repository.weatherRepository.WeatherRepository
import com.example.wecli.dataLayer.response.Resource
import com.example.wecli.domainLayer.useCase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class GetWeatherUserUseCase(
    private val weatherRepository: WeatherRepository
) : UseCase<Pair<Double, Double>, WeatherResponse?>() {

    override suspend fun doWork(params: Pair<Double, Double>): Resource<WeatherResponse?> {
        val (lat, lon) = params
        return weatherRepository.fetchWeather(lat, lon)
            .map { weatherResource ->
                when (weatherResource) {
                    is Resource.Success -> Resource.Success(weatherResource.data)
                    is Resource.Error -> Resource.Error(weatherResource.message.toString())
                    is Resource.Loading -> Resource.Loading()
                }
            }.last()
    }

    operator fun invoke(lat: Double, lon: Double): Flow<Resource<WeatherResponse?>> = flow {
        emit(doWork(lat to lon))
    }
}