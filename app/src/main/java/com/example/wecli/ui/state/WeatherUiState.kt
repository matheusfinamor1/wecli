package com.example.wecli.ui.state

data class WeatherUiState(
    val description: String? = null,
    val temp: Double? = null,
    val feelsLike: Double? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val visibility: Int? = null,
    val windSpeed: Double? = null,
    val cloudsAll: Int? = null,
    val country: String? = null,
    val name: String? = null,
    val error: String? = null,
    val isLoading: Boolean = true
)
