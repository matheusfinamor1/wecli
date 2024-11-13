package com.matheusfinamor.wecli.dataLayer.di

import android.net.ConnectivityManager
import com.matheusfinamor.wecli.dataLayer.connection.ConnectivityObserver
import com.matheusfinamor.wecli.dataLayer.connection.NetworkConnectivityObserver
import com.matheusfinamor.wecli.dataLayer.repository.forecastRepository.ForecastRepository
import com.matheusfinamor.wecli.dataLayer.repository.forecastRepository.ForecastRepositoryImpl
import com.matheusfinamor.wecli.dataLayer.repository.hourRepository.HourRepository
import com.matheusfinamor.wecli.dataLayer.repository.hourRepository.HourRepositoryImpl
import com.matheusfinamor.wecli.dataLayer.repository.weatherRepository.WeatherRepository
import com.matheusfinamor.wecli.dataLayer.repository.weatherRepository.WeatherRepositoryImpl
import com.matheusfinamor.wecli.dataLayer.service.forecastService.ForecastService
import com.matheusfinamor.wecli.dataLayer.service.forecastService.ForecastServiceImpl
import com.matheusfinamor.wecli.dataLayer.service.weatherCurrentService.WeatherService
import com.matheusfinamor.wecli.dataLayer.service.weatherCurrentService.WeatherServiceImpl
import com.matheusfinamor.wecli.domainLayer.core.DeviceLanguage
import com.matheusfinamor.wecli.domainLayer.core.LocationUserManager
import com.matheusfinamor.wecli.domainLayer.useCase.combinedWeatherUseCase.GetCombinedWeatherUseCase
import com.matheusfinamor.wecli.domainLayer.useCase.forecastUseCase.GetForecastUseCase
import com.matheusfinamor.wecli.domainLayer.useCase.momentDayUseCase.GetMomentDayUseCase
import com.matheusfinamor.wecli.domainLayer.useCase.weatherUseCase.GetWeatherUserUseCase
import com.matheusfinamor.wecli.uiLayer.ui.viewmodel.WeatherViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.conscrypt.Conscrypt
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import javax.net.ssl.SSLContext

val appModules = module {
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single<ForecastRepository> { ForecastRepositoryImpl(get()) }
    single<WeatherService> { WeatherServiceImpl(get(), get()) }
    single<ForecastService> { ForecastServiceImpl(get(), get()) }
    single<HourRepository> { HourRepositoryImpl() }
    single<ConnectivityObserver> { NetworkConnectivityObserver(get()) }
    single { GetMomentDayUseCase(get()) }
    single { GetWeatherUserUseCase(get()) }
    single { GetCombinedWeatherUseCase(get(), get()) }
    single { GetForecastUseCase(get()) }
    single { LocationUserManager(get()) }
    singleOf(::DeviceLanguage)
    viewModel { WeatherViewModel(get(), get()) }
}

val androidModule = module {
    single {
        androidContext().getSystemService(ConnectivityManager::class.java)
                as ConnectivityManager
    }
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
