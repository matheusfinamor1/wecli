package com.matheusfinamor.wecli.dataLayer.data.weatherCurrentData

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lon: Double? = null,
    val lat: Double? = null
)
