package com.example.wecli.di

import com.example.wecli.core.LocationUserManager
import com.example.wecli.repository.forecastRepository.ForecastRepositoryImpl
import com.example.wecli.repository.hourRepository.HourRepository
import com.example.wecli.repository.hourRepository.HourRepositoryImpl
import com.example.wecli.repository.weatherRepository.WeatherRepositoryImpl
import com.example.wecli.service.forecastService.ForecastService
import com.example.wecli.service.forecastService.ForecastServiceImpl
import com.example.wecli.service.weatherCurrentService.WeatherService
import com.example.wecli.service.weatherCurrentService.WeatherServiceImpl
import com.example.wecli.ui.viewmodel.WeatherViewModel
import com.example.wecli.useCase.combinedWeatherUseCase.GetCombinedWeatherUseCase
import com.example.wecli.useCase.forecastUseCase.GetForecastUseCase
import com.example.wecli.useCase.momentDayUseCase.GetMomentDayUseCase
import com.example.wecli.useCase.weatherUseCase.GetWeatherUserUseCase
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
    single<WeatherRepositoryImpl> { WeatherRepositoryImpl(get()) }
    single<ForecastRepositoryImpl> { ForecastRepositoryImpl(get()) }
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
