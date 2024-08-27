package com.example.wecli.uiLayer.utils.extensions

import com.example.wecli.dataLayer.data.forecastData.ForecastResponse
import com.example.wecli.dataLayer.data.weatherCurrentData.WeatherResponse
import com.example.wecli.uiLayer.ui.state.WeatherUiState

fun WeatherResponse.toUiState(forecastResponse: ForecastResponse?): WeatherUiState {
    return this.let {
        WeatherUiState(
            description = it.weather?.get(0)?.description,
            temp = it.main?.temp?.toInt(),
            feelsLike = it.main?.feels_like?.toInt(),
            pressure = it.main?.pressure,
            humidity = it.main?.humidity,
            windSpeed = it.wind?.speed?.toInt(),
            cloudsAll = it.clouds?.all,
            country = it.sys?.country,
            name = it.name,
            forecastCnt = forecastResponse?.cnt,
            icon = it.weather?.get(0)?.icon
        )
    }
}