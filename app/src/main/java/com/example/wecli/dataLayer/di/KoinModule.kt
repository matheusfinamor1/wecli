package com.example.wecli.dataLayer.di

import com.example.wecli.domainLayer.core.LocationUserManager
import com.example.wecli.dataLayer.repository.forecastRepository.ForecastRepository
import com.example.wecli.dataLayer.repository.forecastRepository.ForecastRepositoryImpl
import com.example.wecli.dataLayer.repository.hourRepository.HourRepository
import com.example.wecli.dataLayer.repository.hourRepository.HourRepositoryImpl
import com.example.wecli.dataLayer.repository.weatherRepository.WeatherRepository
import com.example.wecli.dataLayer.repository.weatherRepository.WeatherRepositoryImpl
import com.example.wecli.dataLayer.service.forecastService.ForecastService
import com.example.wecli.dataLayer.service.forecastService.ForecastServiceImpl
import com.example.wecli.dataLayer.service.weatherCurrentService.WeatherService
import com.example.wecli.dataLayer.service.weatherCurrentService.WeatherServiceImpl
import com.example.wecli.uiLayer.ui.viewmodel.WeatherViewModel
import com.example.wecli.domainLayer.useCase.combinedWeatherUseCase.GetCombinedWeatherUseCase
import com.example.wecli.domainLayer.useCase.forecastUseCase.GetForecastUseCase
import com.example.wecli.domainLayer.useCase.momentDayUseCase.GetMomentDayUseCase
import com.example.wecli.domainLayer.useCase.weatherUseCase.GetWeatherUserUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.conscrypt.Conscrypt
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import javax.net.ssl.SSLContext

val appModules = module {
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single<ForecastRepository> { ForecastRepositoryImpl(get()) }
    single { GetMomentDayUseCase(get()) }
    single { GetWeatherUserUseCase(get()) }
    single { GetCombinedWeatherUseCase(get(), get()) }
    single { GetForecastUseCase(get()) }
    single<WeatherService> { WeatherServiceImpl(get()) }
    single<ForecastService> { ForecastServiceImpl(get()) }
    single<HourRepository> { HourRepositoryImpl() }
    single { LocationUserManager(get()) }
    viewModel { WeatherViewModel(get(), get()) }
}

val networkModule = module {
    single {
        initializeConscrypt()
        HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                    contentType = ContentType.Application.Json
                )
            }
        }
    }
}

private fun initializeConscrypt() {
    val sslContext = SSLContext.getInstance("TLS", Conscrypt.newProvider())
    sslContext.init(null, null, null)
    SSLContext.setDefault(sslContext)
}
