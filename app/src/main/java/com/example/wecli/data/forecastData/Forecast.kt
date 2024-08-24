package com.example.wecli.data.forecastData

import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<ForecastWeather>,
    val clouds: ForecastClouds,
    val wind: ForecastWind,
    val visibility: Int,
    val pop: Double,
    val sys: ForecastSys,
    val dt_txt: String
)
