package com.example.wecli.dataLayer.service.forecastService

import com.example.wecli.BuildConfig
import com.example.wecli.dataLayer.data.forecastData.ForecastResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ForecastServiceImpl(
    private val client: HttpClient
) : ForecastService {
    private val apiKey = BuildConfig.apiKey

    override suspend fun fetchForecast(lat: Double, lon: Double): ForecastResponse =
        client.get("$FORECAST_BASE_URL?lat=${lat}&lon=${lon}&appid=${apiKey}&lang=$LANGUAGE&units=$UNITS&cnt=$CNT")
            .body()

    companion object {
        private const val FORECAST_BASE_URL =
            "https://api.openweathermap.org/data/2.5/forecast"
        private const val LANGUAGE = "pt_br"
        private const val UNITS = "metric"
        private const val CNT = 40
    }
}
