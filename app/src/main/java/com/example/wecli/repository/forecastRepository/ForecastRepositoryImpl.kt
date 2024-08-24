package com.example.wecli.repository.forecastRepository

import com.example.wecli.data.forecastData.ForecastResponse
import com.example.wecli.response.Resource
import com.example.wecli.service.forecastService.ForecastService
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