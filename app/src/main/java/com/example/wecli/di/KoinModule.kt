package com.example.wecli.di

import com.example.wecli.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SplashScreenViewModel() }
}