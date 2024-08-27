package com.example.wecli.domainLayer.useCase.forecastUseCase

import com.example.wecli.dataLayer.data.forecastData.ForecastResponse
import com.example.wecli.dataLayer.repository.forecastRepository.ForecastRepository
import com.example.wecli.dataLayer.response.Resource
import com.example.wecli.domainLayer.useCase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class GetForecastUseCase(
    private val forecastRepository: ForecastRepository
) : UseCase<Pair<Double, Double>, ForecastResponse?>() {

    override suspend fun doWork(params: Pair<Double, Double>): Resource<ForecastResponse?> {
        val (lat, lon) = params
        return forecastRepository.fetchForecast(lat, lon)
            .map { forecastResource ->
                when (forecastResource) {
                    is Resource.Success -> Resource.Success(forecastResource.data)
                    is Resource.Error -> Resource.Error(forecastResource.message.toString())
                    is Resource.Loading -> Resource.Loading()
                }
            }.last()
    }

    operator fun invoke(lat: Double, lon: Double): Flow<Resource<ForecastResponse?>> = flow {
        emit(doWork(lat to lon))
    }

}