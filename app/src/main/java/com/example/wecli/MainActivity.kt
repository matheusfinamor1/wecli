package com.example.wecli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wecli.ui.screens.WeatherScreen
import com.example.wecli.ui.theme.WecliTheme
import com.example.wecli.ui.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        installSplashScreen()
        setContent {
            WecliTheme {
                val viewModel: WeatherViewModel = koinViewModel()
                WeatherScreen(fusedLocationClient, viewModel)
            }
        }
    }
}