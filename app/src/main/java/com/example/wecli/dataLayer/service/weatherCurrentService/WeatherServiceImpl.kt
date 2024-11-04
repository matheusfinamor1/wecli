package com.example.wecli.dataLayer.service.weatherCurrentService

import com.example.wecli.BuildConfig
import com.example.wecli.dataLayer.data.weatherCurrentData.WeatherResponse
import com.example.wecli.domainLayer.core.DeviceLanguage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WeatherServiceImpl(
    private val client: HttpClient,
    private val deviceLanguage: DeviceLanguage
) : WeatherService {
    private val apiKey = BuildConfig.apiKey
    override suspend fun fetchWeather(lat: Double, lon: Double): WeatherResponse =
    client.get("$BASE_URL?lat=${lat}&lon=${lon}&appid=${apiKey}&lang=${deviceLanguage.getDeviceLanguage()}&units=$UNITS").body()

    companion object {
        private const val BASE_URL =
            "https://api.openweathermap.org/data/2.5/weather"
        private const val UNITS = "metric"
    }
}
