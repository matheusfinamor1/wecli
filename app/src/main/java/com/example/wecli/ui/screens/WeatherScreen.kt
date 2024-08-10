package com.example.wecli.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.wecli.ui.state.WeatherUiState
import com.example.wecli.ui.theme.Blue
import com.example.wecli.ui.theme.BlueNight
import com.example.wecli.ui.theme.BrownAfternoon
import com.example.wecli.ui.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@Composable
fun WeatherScreen(
    fusedLocationClient: FusedLocationProviderClient,
    viewModel: WeatherViewModel,
    uiState: WeatherUiState,
    momentDay: String
) {
    val context = LocalContext.current
    val showPermissionRequest = remember { mutableStateOf(false) }
    RequestPermission(context, showPermissionRequest, fusedLocationClient, viewModel)
    ShowDialog(showPermissionRequest, context)
    ContentScreen(uiState, momentDay)
}

@Composable
fun ContentScreen(uiState: WeatherUiState, momentDay: String) {
    val background = when (momentDay) {
        "Morning" -> {
            Blue
        }

        "Afternoon" -> {
            BrownAfternoon
        }

        else -> {
            BlueNight
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        uiState.let { attr ->
            attr.description?.let { Text(text = "Descrição: $it") }
            attr.temp?.let { Text(text = "Temperatura: $it ºC") }
            attr.feelsLike?.let { Text(text = "Sensação térmica: $it ºC") }
            attr.pressure?.let { Text(text = "Pressão atmosférica: $it hPa") }
            attr.humidity?.let { Text(text = "Umidade: $it%") }
            attr.visibility?.let { Text(text = "Visibilidade: $it") }
            attr.windSpeed?.let { Text(text = "Velocidade do vento: $it km/h") }
            attr.cloudsAll?.let { Text(text = "Nebulosidade: $it%") }
            attr.country?.let { Text(text = "Pais: $it") }
            attr.name?.let { Text(text = "Cidade: $it") }
        }
        if (uiState.error != null) {
            Text(text = uiState.error)
        }
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
    }

}

@Composable
private fun ShowDialog(
    showPermissionRequest: MutableState<Boolean>,
    context: Context
) {
    if (showPermissionRequest.value) {
        AlertDialog(
            title = {
                Text(text = "Autorize o acesso a localização")
            },
            text = {
                Text(text = "Para um bom uso do aplicativo, é necessaria a autorização do uso da localização do seu dispositivo.")
            },
            onDismissRequest = {
                showPermissionRequest.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPermissionRequest.value = false
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }.also {
                            startActivity(context, it, null)
                        }
                    }
                ) {
                    Text("Ir para configurações")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPermissionRequest.value = false
                    }
                ) {
                    Text("Fechar")
                }
            }
        )
    }
}

@Composable
private fun RequestPermission(
    context: Context,
    showPermissionRequest: MutableState<Boolean>,
    fusedLocationClient: FusedLocationProviderClient,
    viewModel: WeatherViewModel
) {
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted.not()) {
                showPermissionRequest.value = true
            } else {
                getCurrentLocation(
                    fusedLocationClient,
                    onGetCurrentLocationSuccess = {
                        viewModel.getWeatherUser(it.second, it.first)
                    },
                    onGetCurrentLocationFailure = {
                        Log.d(
                            "Response",
                            "RequestPermission: $it"
                        )
                    },
                    context = context
                )
            }
        }
    )
    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
            .let { isGranted ->
                if (isGranted == PackageManager.PERMISSION_DENIED)
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                else
                    getCurrentLocation(
                        fusedLocationClient,
                        onGetCurrentLocationSuccess = {
                            viewModel.getWeatherUser(it.second, it.first)
                        },
                        onGetCurrentLocationFailure = {
                            Log.d(
                                "Response",
                                "RequestPermission: $it"
                            )
                        },
                        context = context
                    )
            }

    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
    onGetCurrentLocationFailure: (Exception) -> Unit,
    priority: Boolean = true,
    context: Context
) {
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY
    if (areLocationPermissionsGranted(context)) {
        fusedLocationClient.getCurrentLocation(
            accuracy, CancellationTokenSource().token,
        ).addOnSuccessListener { location ->
            location?.let {
                onGetCurrentLocationSuccess(Pair(it.latitude, it.longitude))
            }
        }.addOnFailureListener { exception ->
            onGetCurrentLocationFailure(exception)
        }
    }
}


private fun areLocationPermissionsGranted(context: Context): Boolean {
    return (ActivityCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
}
