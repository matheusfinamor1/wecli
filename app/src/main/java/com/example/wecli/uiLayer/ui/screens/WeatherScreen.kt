@file:OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)

package com.example.wecli.uiLayer.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults.colors
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.wecli.domainLayer.core.LocationUserManager
import com.example.wecli.uiLayer.ui.state.ListForecastUiState
import com.example.wecli.uiLayer.ui.state.WeatherUiState
import com.example.wecli.uiLayer.ui.theme.Blue
import com.example.wecli.uiLayer.ui.theme.BlueNight
import com.example.wecli.uiLayer.ui.theme.BlueNightToWhiteGradient
import com.example.wecli.uiLayer.ui.theme.BlueToWhiteGradient
import com.example.wecli.uiLayer.ui.theme.BrownAfternoon
import com.example.wecli.uiLayer.ui.theme.BrownToWhiteGradient
import com.example.wecli.uiLayer.ui.theme.White
import com.example.wecli.uiLayer.ui.theme.openSansFontFamily
import com.example.wecli.uiLayer.ui.viewmodel.WeatherViewModel
import com.example.wecli.uiLayer.utils.ApiEndpoint
import com.example.wecli.uiLayer.utils.DateFormats
import com.example.wecli.uiLayer.utils.formatter.toFormattedDate
import com.google.android.gms.location.FusedLocationProviderClient
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.TimeZone

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
    val filteredForecastList: WeatherUiState by viewModel.listFilterDays.collectAsState(initial = uiState)

    locationManager.RequestPermission(context,
        showPermissionRequest,
        fusedLocationClient,
        onGetCurrentLocationSuccess = { latitude, longitude ->
            viewModel.getCombinedWeather(latitude, longitude)

        },
        onGetCurrentLocationFailure = { exception ->
            Log.e("Response", "WeatherScreen: $exception")
        })
    ShowDialog(showPermissionRequest, context)
    ContentScreen(uiState, momentDay, viewModel, filteredForecastList)
}

@Composable
private fun ContentScreen(
    uiState: WeatherUiState,
    momentDay: String,
    viewModel: WeatherViewModel,
    filteredForecastList: WeatherUiState
) {
    val background = defineBackgroundColorForScreen(momentDay)
    val scrollStateScreen = rememberScrollState()
    CreateRememberDatePicker()
    when (uiState.isLoading) {
        true -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = background)
                    .verticalScroll(scrollStateScreen),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                CircularProgressIndicator()
            }
        }

        false -> {
            val backgroundLayoutComposable: Color =
                defineBackgroundColorForLayoutComposable(background)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = background)
                        .verticalScroll(scrollStateScreen),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Spacer(Modifier.height(32.dp))
                    ContentLocation(uiState)
                    Spacer(Modifier.height(16.dp))
                    ContentTemp(uiState)
                    Spacer(Modifier.height(16.dp))
                    ContentDescriptionAndThermalSensation(uiState, backgroundLayoutComposable)
                    Spacer(Modifier.height(32.dp))
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
                    Spacer(Modifier.height(32.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        DatePickerWithDialog(viewModel)
                    }
                    LazyRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        items(filteredForecastList.forecastList?.size ?: 0) { index ->
                            filteredForecastList.forecastList?.let {
                                ItemLazyRow(
                                    it,
                                    index,
                                    onClick = { Log.d("Response", "Item ${it[index]} clicado") })

                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun ItemLazyRow(
    it: List<ListForecastUiState>,
    index: Int,
    onClick: () -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxSize(0.5f)
            .border(
                color = Color.LightGray,
                shape = ShapeDefaults.Small,
                width = 2.dp
            )
            .padding(8.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DateForecastContent(it, index)
            Spacer(modifier = Modifier.width(6.dp))
            HourForecastContent(it, index)
        }
        ForecastTempContent(it, index)
        Text(
            text = it[index].weatherForecastUiState?.get(0)?.forecastWeatherDescription.toString()
                .replaceFirstChar {
                    it.uppercase()
                }
        )
    }
}

@Composable
private fun HourForecastContent(
    it: List<ListForecastUiState>,
    index: Int
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            painter = painterResource(R.drawable.ic_schedule),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
                .padding(end = 4.dp),
        )
        Text(
            text = it[index].hourForecastUiState.toString(),
        )
    }
}

@Composable
private fun DateForecastContent(
    it: List<ListForecastUiState>,
    index: Int
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_calendar_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
                .padding(end = 4.dp),
        )
        Text(
            text = it[index].dataForecastUiState.toString(),
        )
    }
}

@Composable
private fun ForecastTempContent(
    it: List<ListForecastUiState>, index: Int
) {
    Row(
        modifier = Modifier.wrapContentSize()
    ) {
        ContentForecastMaxTemp(it, index)
        GlideImage(
            model = "${ApiEndpoint.BASE_ENDPOINT_IMAGE}${
                it[index].weatherForecastUiState?.get(
                    0
                )?.forecastWeatherIcon
            }@2x.png", contentDescription = null
        )
        ContentForecastMinTemp(it, index)
    }
}

@Composable
private fun CreateRememberDatePicker() {
    rememberDatePickerState(selectableDates = object : SelectableDates {
        val zoneId = TimeZone.getTimeZone(DateFormats.TIME_ZONE).toZoneId()
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val selectedDate = Instant.ofEpochMilli(utcTimeMillis).atZone(zoneId)
            val currentDate = ZonedDateTime.now(zoneId)
            val fiveDaysRange = currentDate.plusDays(5)
            return selectedDate.isBefore(fiveDaysRange) && selectedDate.isAfter(
                currentDate
            )
        }

        override fun isSelectableYear(year: Int): Boolean {
            return true
        }
    })
}

@Composable
private fun DatePickerWithDialog(viewModel: WeatherViewModel) {
    val selectedData = remember { mutableStateOf("Todos") }
    val showDialog = remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        val zoneId = TimeZone.getTimeZone(DateFormats.TIME_ZONE).toZoneId()
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val selectedDate = Instant.ofEpochMilli(utcTimeMillis).atZone(zoneId).toLocalDate()
            val today = LocalDate.now(ZoneId.of(DateFormats.TIME_ZONE))
            val fiveDaysRange = today.plusDays(5)
            return !selectedDate.isBefore(today) && !selectedDate.isAfter(fiveDaysRange)
        }

        override fun isSelectableYear(year: Int): Boolean {
            return true
        }
    })

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            selectedData.value = it.toFormattedDate()
        }
    }

    Button(onClick = { showDialog.value = true }) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.icon_calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 4.dp)
            )
            Text(
                text = selectedData.value, color = Color.Black
            )
        }
    }

    if (showDialog.value) {
        DatePickerDialog(onDismissRequest = { showDialog.value = false }, confirmButton = {
            Row(modifier = Modifier.wrapContentWidth()) {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.filterDayForecast("Todos")
                        selectedData.value = "Todos"
                    }
                    showDialog.value = false
                }
                ) {
                    Text(text = "Todos os dias", color = Color.Black)
                }
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.filterDayForecast(selectedData.value)
                    }
                    showDialog.value = false
                }) {
                    Text(text = "Confirmar", color = Color.Black)
                }

            }

        }, dismissButton = {
            TextButton(onClick = { showDialog.value = false }) {
                Text(text = "Cancelar", color = Color.Black)
            }
        },
            content = {
                DatePicker(
                    state = datePickerState, colors = colors(
                        titleContentColor = Color.Black,
                        todayContentColor = Color.DarkGray,
                        selectedDayContainerColor = Color.LightGray,
                        selectedDayContentColor = Color.Black,
                    ),
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp, top = 12.dp, bottom = 4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Selecione uma data para filtrar", fontSize = 18.sp)
                        }
                    }
                )
            }
        )
    }
}

@Composable
private fun ContentForecastMinTemp(
    forecastList: List<ListForecastUiState>, index: Int
) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .padding(6.dp)
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.icon_thermometer_down),
            contentDescription = null
        )
        Text(text = "${forecastList[index].mainForecastUiState?.forecastMainTempMin}")
    }
}

@Composable
private fun ContentForecastMaxTemp(
    forecastList: List<ListForecastUiState>, index: Int
) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .padding(6.dp)
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.icon_thermometer_up),
            contentDescription = null
        )
        Text(text = "${forecastList[index].mainForecastUiState?.forecastMainTempMax}")
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
                    text = "$temp", fontSize = 64.sp, color = White, fontFamily = openSansFontFamily
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
    uiState: WeatherUiState, backgroundLayoutComposable: Color
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
                if (it != null) {
                    GlideImage(
                        model = "${ApiEndpoint.BASE_ENDPOINT_IMAGE}$it@2x.png",
                        contentDescription = null,
                    )
                } else {
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
    backgroundLayoutComposable: Color, uiState: WeatherUiState, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                color = Color.LightGray, shape = ShapeDefaults.Small, width = 2.dp
            )
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
                    text = "$it${stringResource(R.string.percent)}", fontFamily = openSansFontFamily
                )
            }
        }
    }
}


@Composable
private fun ContentWindSpeedAndCloudiness(
    backgroundLayoutComposable: Color, uiState: WeatherUiState, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                color = Color.LightGray, shape = ShapeDefaults.Small, width = 2.dp
            )
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
                    text = "$it${stringResource(R.string.percent)}", fontFamily = openSansFontFamily
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
    showPermissionRequest: MutableState<Boolean>, context: Context
) {
    if (showPermissionRequest.value) {
        AlertDialog(title = {
            Text(text = stringResource(R.string.title_message_aut_location))
        }, text = {
            Text(text = stringResource(R.string.message_aut_location))
        }, onDismissRequest = {
            showPermissionRequest.value = false
        }, confirmButton = {
            TextButton(
                onClick = {
                    showPermissionRequest.value = false
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }.also {
                        startActivity(context, it, null)
                    }
                }, colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Text(stringResource(R.string.message_go_config))
            }
        }, dismissButton = {
            TextButton(
                onClick = {
                    showPermissionRequest.value = false
                }, colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Text(stringResource(R.string.message_close))
            }
        })
    }
}

