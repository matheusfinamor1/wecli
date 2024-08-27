package com.example.wecli.uiLayer.utils.extensions

import com.example.wecli.dataLayer.data.forecastData.ForecastResponse
import com.example.wecli.uiLayer.ui.state.WeatherUiState

fun ForecastResponse.toUiState(): WeatherUiState {
    return this.let {
        WeatherUiState().copy(
//            forecastMainTemp = it.list[0].main.temp,
//            forecastMainFeelsLike = it.list[0].main.feels_like,
//            forecastMainTempMin = it.list[0].main.temp_min,
//            forecastMainTempMax = it.list[0].main.temp_max,
//            forecastMainPressure = it.list[0].main.pressure,
//            forecastMainHumidity = it.list[0].main.humidity,
//            forecastWeatherId = it.list[0].weather[0].id,
//            forecastWeatherMain = it.list[0].weather[0].main,
//            forecastWeatherDescription = it.list[0].weather[0].description,
//            forecastWeatherIcon = it.list[0].weather[0].icon,
//            forecastCloudsAll = it.list[0].clouds.all,
//            forecastWindSpeed = it.list[0].wind.speed,
//            forecastVisibility = it.list[0].visibility,
//            forecastPop = it.list[0].pop,
//            forecastSysPod = it.list[0].sys.pod,
//            forecastDtTxt = it.list[0].dt_txt

        )
    }
}