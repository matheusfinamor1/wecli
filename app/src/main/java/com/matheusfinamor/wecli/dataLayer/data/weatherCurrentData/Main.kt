package com.matheusfinamor.wecli.dataLayer.data.weatherCurrentData

import kotlinx.serialization.Serializable

@Serializable
data class Main(
    val temp: Double? = null,
    val feels_like: Double? = null,
    val temp_min: Double? = null,
    val temp_max: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val sea_level: Int? = null,
    val grnd_level: Int? = null
)
