@file:OptIn(ExperimentalGlideComposeApi::class)

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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.wecli.R
import com.example.wecli.ui.state.WeatherUiState
import com.example.wecli.ui.theme.BlueNightToWhiteGradient
import com.example.wecli.ui.theme.BlueToWhiteGradient
import com.example.wecli.ui.theme.BrownToWhiteGradient
import com.example.wecli.ui.theme.White
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
    val background = defineBackgroundColor(momentDay)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        ContentLocation(uiState)
        Spacer(Modifier.height(8.dp))
        ContentTemp(uiState)
        Spacer(Modifier.height(8.dp))
        ContentDescriptionAndThermalSensation(background, uiState)
        ContentHumidityAndAtmosphericPressure(background, uiState)

        uiState.let { attr ->
            attr.visibility?.let { Text(text = "Visibilidade: $it") }
            attr.windSpeed?.let { Text(text = "Velocidade do vento: $it km/h") }
            attr.cloudsAll?.let { Text(text = "Nebulosidade: $it%") }
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
private fun ContentHumidityAndAtmosphericPressure(
    background: Brush,
    uiState: WeatherUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .border(brush = background, shape = ShapeDefaults.Medium, width = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_pressao_atm),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 8.dp)
                )
                uiState.pressure?.let {
                    Text("$it hPa")
                }
            }

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_umidade),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 8.dp)
                )
                uiState.humidity?.let {
                    Text("$it%")
                }
            }
        }

    }
}

@Composable
private fun ContentDescriptionAndThermalSensation(
    background: Brush,
    uiState: WeatherUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(brush = background, shape = CircleShape, width = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                model = "https://openweathermap.org/img/wn/${uiState.icon}@2x.png",
                contentDescription = null
            )
            uiState.description?.let { description ->
                Text(
                    text = description.replaceFirstChar { it.uppercase() },
                    modifier = Modifier.fillMaxWidth(0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_temp),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            uiState.feelsLike?.let {
                Text(
                    text = "$it ºC",
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(end = 12.dp),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ContentTemp(uiState: WeatherUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        uiState.let {
            it.temp?.let { temp ->
                Text(
                    text = "$temp",
                    fontSize = 64.sp,
                    color = White
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("ºC", modifier = Modifier.padding(top = 10.dp), color = White)
            }

        }
    }
}

@Composable
private fun ContentLocation(uiState: WeatherUiState) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        uiState.let {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            it.name?.let { text -> Text(text) }
            it.country?.let { text -> Text(", $text") }
        }
    }
}

@Composable
private fun defineBackgroundColor(momentDay: String): Brush {
    val background = when (momentDay) {
        "Morning" -> {
            BlueToWhiteGradient
        }

        "Afternoon" -> {
            BrownToWhiteGradient
        }

        else -> {
            BlueNightToWhiteGradient
        }

    }
    return background
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
