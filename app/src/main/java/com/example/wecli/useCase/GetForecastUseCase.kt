package com.example.wecli.useCase

import android.util.Log
import com.example.wecli.data.forecastData.ForecastResponse
import com.example.wecli.repository.forecastRepository.ForecastRepositoryImpl
import com.example.wecli.response.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetForecastUseCase(
    private val forecastRepository: ForecastRepositoryImpl
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Resource<ForecastResponse>> = flow {
        forecastRepository.fetchForecast(lat, lon).collect { forecastResource ->
            when (forecastResource) {
                is Resource.Success -> {
                    Log.d("ForecastResponse", "${forecastResource.data} ")
                }

                is Resource.Error -> {
                    Log.d("ForecastResponse", "${forecastResource.message} ")
                }

                is Resource.Loading -> Log.d("ForecastResponse", "Carregando...")
            }
        }

    }
}