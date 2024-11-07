package com.example.wecli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wecli.dataLayer.connection.ConnectivityObserver
import com.example.wecli.domainLayer.core.LocationUserManager
import com.example.wecli.uiLayer.ui.screens.SplashScreen
import com.example.wecli.uiLayer.ui.screens.SplashScreenRoute
import com.example.wecli.uiLayer.ui.screens.WeatherScreen
import com.example.wecli.uiLayer.ui.theme.WecliTheme
import com.example.wecli.uiLayer.ui.viewmodel.WeatherViewModel
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
                val connectivityObserver: ConnectivityObserver = getKoin().get()
                val status by connectivityObserver.observer().collectAsState(
                    initial = ConnectivityObserver.Status.Unavailable
                )
                val message = when (status) {
                    ConnectivityObserver.Status.Available -> stringResource(R.string.text_with_internet)
                    ConnectivityObserver.Status.Unavailable -> null
                    ConnectivityObserver.Status.Losing -> stringResource(R.string.text_losing_connection)
                    ConnectivityObserver.Status.Lost -> stringResource(R.string.text_lost_internet)
                }
                val snackbarHostState = remember { SnackbarHostState() }
                LaunchedEffect(message) {
                    message?.let {
                        snackbarHostState.showSnackbar(
                            it,
                            withDismissAction = status == ConnectivityObserver.Status.Lost
                        )
                    }
                }
                val navController = rememberNavController()
                val viewModel: WeatherViewModel = koinViewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val momentDay by viewModel.momentDay.collectAsStateWithLifecycle()
                val locationManager: LocationUserManager = getKoin().get()

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = SplashScreenRoute,
                        Modifier.padding(it)
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
}


@Serializable
object WeatherScreenRoute