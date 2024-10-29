package com.example.wecli.dataLayer.data.forecastData

import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<ForecastWeather>,
    val clouds: ForecastClouds,
    val wind: ForecastWind,
    val pop: Double,
    val sys: ForecastSys,
    val dt_txt: String
)
