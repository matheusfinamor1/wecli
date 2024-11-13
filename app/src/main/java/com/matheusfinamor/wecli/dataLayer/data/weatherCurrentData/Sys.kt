package com.matheusfinamor.wecli.dataLayer.data.weatherCurrentData

import kotlinx.serialization.Serializable

@Serializable
data class Sys(
    val country: String? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null
)
