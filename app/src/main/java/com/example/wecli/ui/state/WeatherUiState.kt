package com.example.wecli.ui.state

data class WeatherUiState(
    val main: String? = null,
    val description: String? = null,
    val base: String? = null,
    val temp: Double? = null,
    val feelsLike: Double? = null,
    val tempMin: Double? = null,
    val tempMax: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val visibility: Int? = null,
    val windSpeed: Double? = null,
    val cloudsAll: Int? = null,
    val country: String? = null,
    val name: String? = null,
)
