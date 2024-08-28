package com.example.wecli.dataLayer.data.forecastData

import kotlinx.serialization.Serializable

@Serializable
data class ForecastCoord(
    val lat: Double,
    val lon: Double
)
