package com.example.wecli.data.forecastData

import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val cnt: Int,
    val list: List<Forecast>,
    val city: City
)
