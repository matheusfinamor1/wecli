package com.example.wecli.ui.state

data class WeatherUiState(
    val main: String = "",
    val description: String = "",
    val base: String = "",
    val temp: Double? = null,
    val feelsLike: Double? = null,
    val tempMin: Double? = null,
    val tempMax: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val visibility: Int? = null,
    val windSpeed: Double? = null,
    val cloudsAll: Int? = null,
    val country: String = "",
    val name: String = "",
)
