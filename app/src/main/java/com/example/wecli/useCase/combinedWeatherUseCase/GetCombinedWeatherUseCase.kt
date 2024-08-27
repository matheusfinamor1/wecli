package com.example.wecli.useCase.combinedWeatherUseCase

import com.example.wecli.response.Resource
import com.example.wecli.ui.state.WeatherUiState
import com.example.wecli.useCase.weatherUseCase.GetWeatherUserUseCase
import com.example.wecli.useCase.forecastUseCase.GetForecastUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetCombinedWeatherUseCase(
    private val getForecastUseCase: GetForecastUseCase,
    private val getWeatherUserUseCase: GetWeatherUserUseCase
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Resource<WeatherUiState>> = combine(
        getForecastUseCase.invoke(lat, lon),
        getWeatherUserUseCase.invoke(lat, lon)
    ) { forecastResponse, weatherResponse ->
        when {
            weatherResponse is Resource.Success && forecastResponse is Resource.Success -> {
                val weatherData = weatherResponse.data!!
                val forecastData = forecastResponse.data

                val combinedWeather = WeatherUiState(
                    description = weatherData.weather?.get(0)?.description,
                    temp = weatherData.main?.temp?.toInt(),
                    feelsLike = weatherData.main?.feels_like?.toInt(),
                    pressure = weatherData.main?.pressure,
                    humidity = weatherData.main?.humidity,
                    visibility = weatherData.visibility,
                    windSpeed = ((weatherData.wind?.speed ?: 0.0) * 3.6).toInt(),
                    cloudsAll = weatherData.clouds?.all,
                    country = weatherData.sys?.country,
                    name = weatherData.name,
                    icon = weatherData.weather?.get(0)?.icon,
                    forecastCnt = forecastData?.cnt,
                    isLoading = false
                )
                Resource.Success(combinedWeather)
            }

            weatherResponse is Resource.Error -> Resource.Error(weatherResponse.message.toString())
            forecastResponse is Resource.Error -> Resource.Error(forecastResponse.message.toString())
            weatherResponse is Resource.Loading || forecastResponse is Resource.Loading -> Resource.Loading()
            else -> Resource.Loading()
        }
    }
}
