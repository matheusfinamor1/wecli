package com.matheusfinamor.wecli.dataLayer.repository.forecastRepository

import com.matheusfinamor.wecli.dataLayer.data.forecastData.ForecastResponse
import com.matheusfinamor.wecli.dataLayer.response.Resource
import com.matheusfinamor.wecli.dataLayer.service.forecastService.ForecastService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ForecastRepositoryImpl(
    private val forecastService: ForecastService
) : ForecastRepository {
    override fun fetchForecast(lat: Double, lon: Double): Flow<Resource<ForecastResponse>> = flow {
       emit(Resource.Loading())
        try{
           val forecast = forecastService.fetchForecast(lat, lon)
           emit(Resource.Success(forecast))
       }catch (e: Exception){
           emit(Resource.Error(e.message.toString()))
       }
    }
}