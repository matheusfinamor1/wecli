package com.example.wecli.service

import com.example.wecli.BuildConfig
import com.example.wecli.data.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class WeatherServiceImpl(
    private val client: HttpClient
) : WeatherService {
    private val apiKey = BuildConfig.apiKey
    override suspend fun fetchWeather(lon: Double, lat: Double): WeatherResponse =
    client.get("$BASE_URL?lat=${lat}&lon=${lon}&appid=${apiKey}&lang=$LANGUAGE&units=$UNITS").body()

    companion object {
        private const val BASE_URL =
            "https://api.openweathermap.org/data/2.5/weather"
        private const val LANGUAGE = "pt_br"
        private const val UNITS = "metric"
    }
}
