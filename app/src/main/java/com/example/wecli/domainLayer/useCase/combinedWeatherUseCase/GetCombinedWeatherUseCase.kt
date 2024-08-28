package com.example.wecli.domainLayer.useCase.combinedWeatherUseCase

import com.example.wecli.uiLayer.utils.mapper.asModel
import com.example.wecli.dataLayer.response.Resource
import com.example.wecli.domainLayer.useCase.forecastUseCase.GetForecastUseCase
import com.example.wecli.domainLayer.useCase.weatherUseCase.GetWeatherUserUseCase
import com.example.wecli.uiLayer.ui.state.WeatherUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCombinedWeatherUseCase(
    private val getForecastUseCase: GetForecastUseCase,
    private val getWeatherUserUseCase: GetWeatherUserUseCase
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Resource<WeatherUiState>> = combine(
        getForecastUseCase.invoke(lat, lon),
        getWeatherUserUseCase.invoke(lat, lon)
    ) { forecastResource, weatherResource ->
        when {
            weatherResource is Resource.Success && forecastResource is Resource.Success -> {
                val weatherUiState: WeatherUiState = Pair(weatherResource.data, forecastResource.data).asModel()
                Resource.Success(weatherUiState)
            }

            weatherResource is Resource.Error -> Resource.Error(weatherResource.message.toString())
            forecastResource is Resource.Error -> Resource.Error(forecastResource.message.toString())
            weatherResource is Resource.Loading || forecastResource is Resource.Loading -> Resource.Loading()
            else -> Resource.Loading()
        }
    }
}

