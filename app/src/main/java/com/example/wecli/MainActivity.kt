package com.example.wecli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wecli.core.LocationUserManager
import com.example.wecli.ui.screens.SplashScreen
import com.example.wecli.ui.screens.SplashScreenRoute
import com.example.wecli.ui.screens.WeatherScreen
import com.example.wecli.ui.theme.WecliTheme
import com.example.wecli.ui.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            WecliTheme {
                val navController = rememberNavController()
                val viewModel: WeatherViewModel = koinViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val momentDay by viewModel.momentDay.collectAsStateWithLifecycle()
                val locationManager: LocationUserManager = getKoin().get()

                NavHost(
                    navController = navController,
                    startDestination = SplashScreenRoute
                ) {
                    composable<SplashScreenRoute> {
                        SplashScreen(
                            onNavigateWeatherScreen = {
                                navController.navigate(WeatherScreenRoute) {
                                    popUpTo(SplashScreenRoute) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable<WeatherScreenRoute> {
                        WeatherScreen(
                            fusedLocationClient = fusedLocationClient,
                            viewModel = viewModel,
                            uiState = uiState,
                            momentDay = momentDay,
                            locationManager = locationManager
                        )
                    }
                }
            }
        }
    }
}


@Serializable
object WeatherScreenRoute