package com.example.wecli.uiLayer.ui.state

data class WeatherUiState(
    val description: String? = null,
    val temp: Int? = null,
    val feelsLike: Int? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val visibility: Int? = null,
    val windSpeed: Int? = null,
    val cloudsAll: Int? = null,
    val country: String? = null,
    val name: String? = null,
    val forecastCnt: Int? = null,
    val error: String? = null,
    val isLoading: Boolean = true,
    val icon: String? = null
)
