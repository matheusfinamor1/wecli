package com.matheusfinamor.wecli.dataLayer.data.forecastData

import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<Forecast>,
    val city: City
)
