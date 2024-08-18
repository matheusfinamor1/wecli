package com.example.wecli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val momentDay by viewModel.momentDay.collectAsStateWithLifecycle()
                WeatherScreen(fusedLocationClient, viewModel, uiState, momentDay)
            }
        }
    }
}
