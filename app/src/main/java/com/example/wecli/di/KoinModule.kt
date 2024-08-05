package com.example.wecli.di

import com.example.wecli.repository.weatherRepository.WeatherRepositoryImpl
import com.example.wecli.repository.hourRepository.HourRepository
import com.example.wecli.repository.hourRepository.HourRepositoryImpl
import com.example.wecli.service.WeatherService
import com.example.wecli.service.WeatherServiceImpl
import com.example.wecli.ui.viewmodel.WeatherViewModel
import com.example.wecli.useCase.GetMomentDayUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single<WeatherRepositoryImpl> { WeatherRepositoryImpl(get()) }
    single { GetMomentDayUseCase(get()) }
    single<WeatherService> { WeatherServiceImpl(get()) }
    single<HourRepository> { HourRepositoryImpl() }
    viewModel { WeatherViewModel(get(), get()) }
}

val networkModule = module {
    single {
        HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
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
