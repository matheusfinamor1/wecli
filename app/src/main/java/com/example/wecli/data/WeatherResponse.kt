package com.example.wecli.data

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val coord: Coord? = null,
    val weather: List<Weather>? = emptyList(),
    val base: String? = null,
    val main: Main? = null,
    val visibility: Int? = null,
    val wind: Wind? = null,
    val clouds: Clouds? = null,
    val dt: Int? = null,
    val sys: Sys? = null,
    val timezone: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    val cod: Int? = null
)