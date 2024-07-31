package com.example.wecli.extensions

import com.example.wecli.data.WeatherResponse
import com.example.wecli.ui.state.WeatherUiState

fun WeatherResponse.toUiState(): WeatherUiState {
    return this.let {
        WeatherUiState(
            main = it.weather[0].main,
            description = it.weather[0].description,
            base = it.base,
            temp = it.main.temp,
            feelsLike = it.main.feels_like,
            tempMin = it.main.temp_min,
            tempMax = it.main.temp_max,
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