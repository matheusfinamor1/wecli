package com.example.wecli.dataLayer.data.forecastData

import kotlinx.serialization.Serializable

@Serializable
data class ForecastWeather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
