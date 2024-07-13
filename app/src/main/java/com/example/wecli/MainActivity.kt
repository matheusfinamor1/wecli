package com.example.wecli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wecli.ui.screens.WeatherScreen
import com.example.wecli.ui.theme.WecliTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            WecliTheme {
                WeatherScreen()
            }
        }
    }
}