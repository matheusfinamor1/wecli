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
    val forecastList: List<ListForecastUiState>? = emptyList(),
    val forecastCitySunrise: String? = null,
    val forecastCitySunset: String? = null,
    val error: String? = null,
    val isLoading: Boolean = true,
    val icon: String? = null
)

data class ListForecastUiState(
    val mainForecastUiState: MainForecastUiState? = null,
    val weatherForecastUiState: List<WeatherForecastUiState>? = emptyList(),
    val cloudsForecastUiState: CloudsForecastUiState? = null,
    val windForecastUiState: WindForecastUiState? = null,
    val visibilityForecastUiState: Int? = null,
    val podForecastUiState: String? = null,
    val dtTxtForecastUiState: String? = null,
    val dataForecastUiState: String? = null,
    val hourForecastUiState: String? = null
)

data class MainForecastUiState(
    val forecastMainTemp: Int? = null,
    val forecastMainFeelsLike: Int? = null,
    val forecastMainTempMin: Int? = null,
    val forecastMainTempMax: Int? = null,
    val forecastMainPressure: Int? = null,
    val forecastMainHumidity: Int? = null
)

data class WeatherForecastUiState(
    val forecastWeatherMain: String? = null,
    val forecastWeatherDescription: String? = null,
    val forecastWeatherIcon: String? = null
)

data class CloudsForecastUiState(
    val forecastCloudsAll: Int? = null
)

data class WindForecastUiState(
    val forecastWindSpeed: Int? = null
)

