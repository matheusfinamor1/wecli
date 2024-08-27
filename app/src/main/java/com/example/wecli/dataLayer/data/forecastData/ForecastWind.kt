package com.example.wecli.dataLayer.data.forecastData

import kotlinx.serialization.Serializable

@Serializable
data class ForecastWind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)
