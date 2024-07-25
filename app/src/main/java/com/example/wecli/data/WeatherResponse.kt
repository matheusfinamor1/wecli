package com.example.wecli.data

data class WeatherResponse(
    val weather: List<Weather>? = emptyList(),
    val main: Main? = null,
    val visibility: Int? = null,
    val wind: Wind? = null,
    val clouds: Clouds? = null,
    val name: String? = null
)