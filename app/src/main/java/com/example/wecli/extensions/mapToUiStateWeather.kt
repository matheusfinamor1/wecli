package com.example.wecli.extensions

import com.example.wecli.data.WeatherResponse
import com.example.wecli.ui.state.WeatherUiState

fun WeatherResponse.toUiState(): WeatherUiState {
    return this.let {
        WeatherUiState(
            description = it.weather[0].description,
            temp = it.main.temp,
            feelsLike = it.main.feels_like,
            pressure = it.main.pressure,
            humidity = it.main.humidity,
            visibility = it.visibility,
            windSpeed = it.wind.speed,
            cloudsAll = it.clouds.all,
            country = it.sys.country,
            name = it.name
        )
    }
}