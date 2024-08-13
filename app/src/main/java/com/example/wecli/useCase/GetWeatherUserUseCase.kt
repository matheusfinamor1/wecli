package com.example.wecli.useCase

import com.example.wecli.extensions.toUiState
import com.example.wecli.repository.weatherRepository.WeatherRepositoryImpl
import com.example.wecli.response.Resource
import com.example.wecli.ui.state.WeatherUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class GetWeatherUserUseCase(
    private val weatherRepository: WeatherRepositoryImpl
) {
    operator fun invoke(lon: Double, lat: Double): Flow<Resource<WeatherUiState>> = flow {
        emit(Resource.Loading())
        weatherRepository.fetchWeather(lon, lat).collect { weatherResource ->
            when (weatherResource) {
                is Resource.Success -> {
                    val weatherResult = weatherResource.data?.toUiState()
                        ?.copy(
                            temp = weatherResource.data.main.temp.toInt(),
                            feelsLike = weatherResource.data.main.feels_like.toInt(),
                            windSpeed = (weatherResource.data.wind.speed * 3.6).toInt(),
                            isLoading = false
                        ) ?: WeatherUiState()
                    emit(Resource.Success(weatherResult))
                }

                is Resource.Error -> emit(Resource.Error(weatherResource.message.toString()))
                is Resource.Loading -> emit(Resource.Loading())
            }
        }

    }
}