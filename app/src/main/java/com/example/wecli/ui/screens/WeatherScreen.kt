@file:OptIn(ExperimentalGlideComposeApi::class)

package com.example.wecli.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.wecli.R
import com.example.wecli.core.LocationUserManager
import com.example.wecli.ui.state.WeatherUiState
import com.example.wecli.ui.theme.Blue
import com.example.wecli.ui.theme.BlueNight
import com.example.wecli.ui.theme.BlueNightToWhiteGradient
import com.example.wecli.ui.theme.BlueToWhiteGradient
import com.example.wecli.ui.theme.BrownAfternoon
import com.example.wecli.ui.theme.BrownToWhiteGradient
import com.example.wecli.ui.theme.White
import com.example.wecli.ui.theme.openSansFontFamily
import com.example.wecli.ui.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
fun WeatherScreen(
    fusedLocationClient: FusedLocationProviderClient,
    viewModel: WeatherViewModel,
    uiState: WeatherUiState,
    momentDay: String,
    locationManager: LocationUserManager
) {
    val context = LocalContext.current
    val showPermissionRequest = remember { mutableStateOf(false) }
    locationManager.RequestPermission(
        context,
        showPermissionRequest,
        fusedLocationClient,
        onGetCurrentLocationSuccess = { latitude, longitude ->
            viewModel.getCombinedWeather(latitude,longitude)

        },
        onGetCurrentLocationFailure = { exception ->
            Log.e("Response", "WeatherScreen: $exception")
        }
    )
    ShowDialog(showPermissionRequest, context)
    ContentScreen(uiState, momentDay)
}

@Composable
private fun ContentScreen(uiState: WeatherUiState, momentDay: String) {
    val background = defineBackgroundColorForScreen(momentDay)
    val scrollState = rememberScrollState()

    when (uiState.isLoading) {
        true -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = background)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                CircularProgressIndicator()
            }
        }

        false -> {
            val backgroundLayoutComposable: Color =
                defineBackgroundColorForLayoutComposable(background)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = background)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Spacer(Modifier.height(64.dp))
                ContentLocation(uiState)
                Spacer(Modifier.height(32.dp))
                ContentTemp(uiState)
                Spacer(Modifier.height(32.dp))
                ContentDescriptionAndThermalSensation(uiState, backgroundLayoutComposable)
                Spacer(Modifier.height(64.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ContentHumidityAndAtmosphericPressure(
                        backgroundLayoutComposable,
                        uiState,
                        Modifier
                            .weight(1f)
                            .padding(start = 6.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    ContentWindSpeedAndCloudiness(
                        backgroundLayoutComposable,
                        uiState,
                        Modifier
                            .weight(1f)
                            .padding(end = 6.dp)
                    )
                }
                Spacer(Modifier.height(64.dp))
            }
        }
    }
}

@Composable
private fun ContentLocation(uiState: WeatherUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        uiState.let {
            Image(
                painter = painterResource(id = R.drawable.icon_loc),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            it.name?.let { text ->
                Text(
                    text = text,
                    fontSize = 20.sp,
                    color = White,
                    fontFamily = openSansFontFamily,
                    fontStyle = Italic
                )
            }
            it.country?.let { text ->
                Text(
                    text = ", $text",
                    fontSize = 20.sp,
                    color = White,
                    fontFamily = openSansFontFamily,
                    fontStyle = Italic
                )
            }
        }
    }
}

@Composable
private fun ContentTemp(
    uiState: WeatherUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        uiState.let {
            it.temp?.let { temp ->
                Text(
                    text = "$temp",
                    fontSize = 64.sp,
                    color = White,
                    fontFamily = openSansFontFamily
                )
            }
            Text(
                text = stringResource(R.string.uni_med_grau_celsius),
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp),
                color = White,
                fontFamily = openSansFontFamily,
                fontWeight = Bold
            )

        }
    }
}

@Composable
private fun ContentDescriptionAndThermalSensation(
    uiState: WeatherUiState,
    backgroundLayoutComposable: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(color = Color.LightGray, shape = ShapeDefaults.Small, width = 2.dp)
            .background(color = backgroundLayoutComposable.copy(alpha = 0.2f)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            uiState.icon.let {
                if(it != null){
                    GlideImage(
                        model = "https://openweathermap.org/img/wn/${it}@2x.png",
                        contentDescription = null,
                    )
                }else{
                    CircularProgressIndicator()
                }
            }

            uiState.description?.let { description ->
                Text(
                    text = description.replaceFirstChar { it.uppercase() },
                    fontFamily = openSansFontFamily,
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
                    fontFamily = openSansFontFamily,
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
private fun ContentHumidityAndAtmosphericPressure(
    backgroundLayoutComposable: Color,
    uiState: WeatherUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(color = Color.LightGray, shape = ShapeDefaults.Small, width = 2.dp)
            .padding(2.dp)
            .background(color = backgroundLayoutComposable.copy(alpha = 0.2f))
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
                Text(
                    text = "$it ${stringResource(R.string.uni_med_atm_press)}",
                    fontFamily = openSansFontFamily
                )
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
                Text(
                    text = "$it${stringResource(R.string.percent)}",
                    fontFamily = openSansFontFamily
                )
            }
        }
    }
}


@Composable
private fun ContentWindSpeedAndCloudiness(
    backgroundLayoutComposable: Color,
    uiState: WeatherUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(color = Color.LightGray, shape = ShapeDefaults.Small, width = 2.dp)
            .padding(2.dp)
            .background(color = backgroundLayoutComposable.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_veloc_vento),
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 8.dp)
            )
            uiState.windSpeed?.let {
                Text(
                    text = "$it ${stringResource(R.string.uni_med_veloc_km_h)}",
                    fontFamily = openSansFontFamily,
                )
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
                painter = painterResource(id = R.drawable.icon_nebulos),
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 8.dp)
            )
            uiState.cloudsAll?.let {
                Text(
                    text = "$it${stringResource(R.string.percent)}",
                    fontFamily = openSansFontFamily
                )
            }
        }
    }
}

@Composable
private fun defineBackgroundColorForScreen(momentDay: String): Brush {
    val background = when (momentDay) {
        stringResource(R.string.periodic_day_morning) -> {
            BlueToWhiteGradient
        }

        stringResource(R.string.periodic_day_afternoon) -> {
            BrownToWhiteGradient
        }

        else -> {
            BlueNightToWhiteGradient
        }

    }
    return background
}

@Composable
private fun defineBackgroundColorForLayoutComposable(background: Brush): Color {
    val backgroundLayoutComposable: Color = when (background) {
        BlueToWhiteGradient -> Blue
        BrownToWhiteGradient -> BrownAfternoon
        else -> BlueNight
    }
    return backgroundLayoutComposable
}

@Composable
private fun ShowDialog(
    showPermissionRequest: MutableState<Boolean>,
    context: Context
) {
    if (showPermissionRequest.value) {
        AlertDialog(
            title = {
                Text(text = stringResource(R.string.title_message_aut_location))
            },
            text = {
                Text(text = stringResource(R.string.message_aut_location))
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
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(stringResource(R.string.message_go_config))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPermissionRequest.value = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(stringResource(R.string.message_close))
                }
            }
        )
    }
}

