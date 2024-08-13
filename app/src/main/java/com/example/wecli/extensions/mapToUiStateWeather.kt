package com.example.wecli.extensions

import com.example.wecli.data.WeatherResponse
import com.example.wecli.ui.state.WeatherUiState

fun WeatherResponse.toUiState(): WeatherUiState {
    return this.let {
        WeatherUiState(
            description = it.weather[0].description,
            temp = it.main.temp.toInt(),
            feelsLike = it.main.feels_like.toInt(),
            pressure = it.main.pressure,
            humidity = it.main.humidity,
            windSpeed = it.wind.speed.toInt(),
            cloudsAll = it.clouds.all,
            country = it.sys.country,
            name = it.name,
            icon = it.weather[0].icon
        )
    }
}