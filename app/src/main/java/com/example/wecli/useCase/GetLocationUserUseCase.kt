package com.example.wecli.useCase

import com.example.wecli.extensions.toUiState
import com.example.wecli.repository.weatherRepository.WeatherRepositoryImpl
import com.example.wecli.response.Resource
import com.example.wecli.ui.state.WeatherUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLocationUserUseCase(
    private val weatherRepository: WeatherRepositoryImpl
) {
    operator fun invoke(lon: Double, lat: Double): Flow<Resource<WeatherUiState>> = flow {
        emit(Resource.Loading())
        try {
            weatherRepository.fetchWeather(lon, lat)
                .collect { weatherResponse ->
                    val weather = weatherResponse.data?.toUiState()?.copy(isLoading = false) ?: WeatherUiState()
                    emit(Resource.Success(weather))
                }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }
}