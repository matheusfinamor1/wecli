package com.matheusfinamor.wecli

import android.app.Application
import com.matheusfinamor.wecli.dataLayer.di.androidModule
import com.matheusfinamor.wecli.dataLayer.di.appModules
import com.matheusfinamor.wecli.dataLayer.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModules, networkModule, androidModule)
        }

    }
}