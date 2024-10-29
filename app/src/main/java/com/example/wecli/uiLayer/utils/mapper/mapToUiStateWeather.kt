package com.example.wecli.uiLayer.utils.mapper

import com.example.wecli.dataLayer.data.forecastData.ForecastResponse
import com.example.wecli.dataLayer.data.weatherCurrentData.WeatherResponse
import com.example.wecli.uiLayer.ui.state.CloudsForecastUiState
import com.example.wecli.uiLayer.ui.state.ListForecastUiState
import com.example.wecli.uiLayer.ui.state.MainForecastUiState
import com.example.wecli.uiLayer.ui.state.WeatherForecastUiState
import com.example.wecli.uiLayer.ui.state.WeatherUiState
import com.example.wecli.uiLayer.ui.state.WindForecastUiState
import com.example.wecli.uiLayer.utils.formatter.DateFormatter.formatData
import com.example.wecli.uiLayer.utils.formatter.DateFormatter.formatDataAndHour
import com.example.wecli.uiLayer.utils.formatter.DateFormatter.formatHour

fun Pair<WeatherResponse?, ForecastResponse?>.asModel(): WeatherUiState {
    val (weatherResource, forecastResource) = this

    val weather = weatherResource?.weather?.firstOrNull()
    val mainWeather = weatherResource?.main
    val wind = weatherResource?.wind
    val clouds = weatherResource?.clouds
    val sys = weatherResource?.sys

    val forecastListUiState = forecastResource?.list?.map { forecastItem ->
        val forecastWeather = forecastItem.weather
        val forecastMain = forecastItem.main
        val forecastClouds = forecastItem.clouds
        val forecastWind = forecastItem.wind

        ListForecastUiState(
            mainForecastUiState = MainForecastUiState(
                forecastMainTemp = forecastMain.temp.toInt(),
                forecastMainFeelsLike = forecastMain.feels_like.toInt(),
                forecastMainTempMin = forecastMain.temp_min.toInt(),
                forecastMainTempMax = forecastMain.temp_max.toInt(),
                forecastMainPressure = forecastMain.pressure,
                forecastMainHumidity = forecastMain.humidity
            ),
            weatherForecastUiState = forecastWeather.map { weatherItem ->
                WeatherForecastUiState(
                    forecastWeatherMain = weatherItem.main,
                    forecastWeatherDescription = weatherItem.description,
                    forecastWeatherIcon = weatherItem.icon
                )
            },
            cloudsForecastUiState = CloudsForecastUiState(
                forecastCloudsAll = forecastClouds.all
            ),
            windForecastUiState = WindForecastUiState(
                forecastWindSpeed = forecastWind.speed.toInt()
            ),
            podForecastUiState = forecastItem.sys.pod,
            dtTxtForecastUiState = forecastItem.dt_txt.formatDataAndHour(),
            dataForecastUiState = forecastItem.dt_txt.formatData(),
            hourForecastUiState = forecastItem.dt_txt.formatHour()
        )
    } ?: emptyList()


    return WeatherUiState(
        description = weather?.description,
        temp = mainWeather?.temp?.toInt(),
        feelsLike = mainWeather?.feels_like?.toInt(),
        pressure = mainWeather?.pressure,
        humidity = mainWeather?.humidity,
        //visibility = weatherResource?.visibility,
        windSpeed = ((wind?.speed ?: 0.0) * 3.6).toInt(),
        cloudsAll = clouds?.all,
        country = sys?.country,
        name = weatherResource?.name,
        forecastList = forecastListUiState,
        forecastCitySunrise = forecastResource?.city?.sunrise.toString(),
        forecastCitySunset = forecastResource?.city?.sunset.toString(),
        icon = weather?.icon,
        isLoading = false
    )
}