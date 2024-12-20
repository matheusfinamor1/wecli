@file:OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)

package com.matheusfinamor.wecli.uiLayer.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.location.FusedLocationProviderClient
import com.matheusfinamor.wecli.R
import com.matheusfinamor.wecli.domainLayer.core.LocationUserManager
import com.matheusfinamor.wecli.uiLayer.ui.components.ItemContentForecastTempMaxAndMin
import com.matheusfinamor.wecli.uiLayer.ui.components.ItemContentWeatherCurrent
import com.matheusfinamor.wecli.uiLayer.ui.components.ItemDescriptionDetails
import com.matheusfinamor.wecli.uiLayer.ui.components.ItemDetailsTempMinAndMax
import com.matheusfinamor.wecli.uiLayer.ui.state.ListForecastUiState
import com.matheusfinamor.wecli.uiLayer.ui.state.WeatherUiState
import com.matheusfinamor.wecli.uiLayer.ui.theme.White
import com.matheusfinamor.wecli.uiLayer.ui.theme.openSansFontFamily
import com.matheusfinamor.wecli.uiLayer.ui.viewmodel.WeatherViewModel
import com.matheusfinamor.wecli.uiLayer.utils.ApiEndpoint
import com.matheusfinamor.wecli.uiLayer.utils.Formats
import com.matheusfinamor.wecli.uiLayer.utils.formatter.toFormattedDate
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
    locationManager: LocationUserManager
) {
    val context = LocalContext.current
    val showPermissionRequest = remember { mutableStateOf(false) }
    val filteredForecastList: WeatherUiState by viewModel.listFilterDays.collectAsStateWithLifecycle()
    val showDialogDetails = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf<ListForecastUiState?>(null) }

    locationManager.RequestPermission(context,
        showPermissionRequest,
        fusedLocationClient,
        onGetCurrentLocationSuccess = { latitude, longitude ->
            viewModel.getCombinedWeather(latitude, longitude)
        },
        onGetCurrentLocationFailure = {})
    PermissionAlertDialog(showPermissionRequest, context)
    DialogDetails(showDialogDetails, selectedItem.value)
    ScreenContent(
        uiState,
        viewModel,
        filteredForecastList,
        showDialogDetails,
        selectedItem
    )
}

@Composable
private fun ScreenContent(
    uiState: WeatherUiState,
    viewModel: WeatherViewModel,
    filteredForecastList: WeatherUiState,
    showDialogDetails: MutableState<Boolean>,
    selectedItem: MutableState<ListForecastUiState?>
) {
    val scrollStateScreen = rememberScrollState()
    CreateRememberDatePicker()
    when (uiState.isLoading) {
        true -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollStateScreen),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                CircularProgressIndicator()
            }
        }

        false -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollStateScreen),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Spacer(Modifier.height(32.dp))
                    ContentLocation(uiState)
                    ContentTemp(uiState)
                    ContentDescriptionAndThermalSensation(uiState)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ItemContentWeatherCurrent(
                            dataFirstLine = Triple(
                                R.drawable.icon_pressao_atm,
                                uiState.pressure,
                                stringResource(R.string.uni_med_atm_press)
                            ),
                            dataSecondLine = Triple(
                                R.drawable.icon_umidade,
                                uiState.humidity,
                                stringResource(R.string.percent)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        ItemContentWeatherCurrent(
                            dataFirstLine = Triple(
                                R.drawable.icon_veloc_vento,
                                uiState.windSpeed,
                                stringResource(R.string.uni_med_veloc_km_h)
                            ),
                            dataSecondLine = Triple(
                                R.drawable.icon_nebulos,
                                uiState.cloudsAll,
                                stringResource(R.string.percent)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 6.dp)

                        )
                    }
                    ContentDate(viewModel)
                    ContentListForecast(filteredForecastList, showDialogDetails, selectedItem)
                }
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
                    text = temp.toString(),
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
    uiState: WeatherUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(color = Color.LightGray, shape = ShapeDefaults.Small, width = 1.dp)
            .clip(RoundedCornerShape(14.dp)),
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
                    overflow = TextOverflow.Ellipsis,
                    color = White
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
                    maxLines = 1,
                    color = White
                )
            }
        }
    }
}

@Composable
private fun ContentDate(viewModel: WeatherViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        DatePickerWithDialog(viewModel)
    }
}

@Composable
private fun ContentListForecast(
    filteredForecastList: WeatherUiState,
    showDialogDetails: MutableState<Boolean>,
    selectedItem: MutableState<ListForecastUiState?>
) {
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
                    showDialogDetails,
                    selectedItem
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun ItemLazyRow(
    it: List<ListForecastUiState>,
    index: Int,
    showDialogDetails: MutableState<Boolean>,
    selectedItem: MutableState<ListForecastUiState?>
) {
    Column(
        modifier = Modifier
            .fillMaxSize(0.5f)
            .border(
                color = Color.LightGray,
                shape = ShapeDefaults.Small,
                width = 2.dp
            )
            .clip(RoundedCornerShape(8.dp))
            .padding(2.dp)
            .clickable(onClick = {
                selectedItem.value = it[index]
                showDialogDetails.value = true
            }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ContentForecastDate(it, index)
            Spacer(modifier = Modifier.width(6.dp))
            ContentTimeForecast(it, index)
        }
        ContentTempForecast(it, index)
        Text(
            text = it[index].weatherForecastUiState?.get(0)?.forecastWeatherDescription.toString()
                .replaceFirstChar {
                    it.uppercase()
                },
            fontFamily = openSansFontFamily,
            color = White
        )
    }
}

@Composable
private fun ContentForecastDate(
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
            fontFamily = openSansFontFamily,
            color = White
        )
    }
}

@Composable
private fun ContentTimeForecast(
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
            fontFamily = openSansFontFamily,
            color = White
        )
    }
}

@Composable
private fun ContentTempForecast(
    it: List<ListForecastUiState>, index: Int
) {
    Row(
        modifier = Modifier.wrapContentSize()
    ) {
        ItemContentForecastTempMaxAndMin(
            temp = it[index].mainForecastUiState?.forecastMainTempMax.toString(),
            image = painterResource(id = R.drawable.icon_thermometer_up)
        )
        GlideImage(
            model = "${ApiEndpoint.BASE_ENDPOINT_IMAGE}${
                it[index].weatherForecastUiState?.get(
                    0
                )?.forecastWeatherIcon
            }@2x.png", contentDescription = null
        )
        ItemContentForecastTempMaxAndMin(
            temp = it[index].mainForecastUiState?.forecastMainTempMin.toString(),
            image = painterResource(id = R.drawable.icon_thermometer_down)
        )
    }
}

@Composable
private fun DatePickerWithDialog(viewModel: WeatherViewModel) {
    val selectedDate = rememberSaveable { mutableStateOf("Todos") }
    val showDateDialog = remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        val zoneId = TimeZone.getTimeZone(Formats.TIME_ZONE).toZoneId()
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val localDate = Instant.ofEpochMilli(utcTimeMillis).atZone(zoneId).toLocalDate()
            val today = LocalDate.now(ZoneId.of(Formats.TIME_ZONE))
            val fiveDaysRange = today.plusDays(5)
            return !localDate.isBefore(today) && !localDate.isAfter(fiveDaysRange)
        }

        override fun isSelectableYear(year: Int): Boolean {
            return true
        }
    })

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            selectedDate.value = it.toFormattedDate()
        }
    }

    Button(
        onClick = { showDateDialog.value = true },
    ) {
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
                text = selectedDate.value, color = Color.Black
            )
        }
    }

    if (showDateDialog.value) {
        DatePickerDialog(
            shape = ShapeDefaults.ExtraLarge,
            onDismissRequest = { showDateDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.filterDayForecast("Todos")
                        selectedDate.value = "Todos"
                    }
                    showDateDialog.value = false
                }
                ) {
                    Text(text = stringResource(R.string.text_all_days), color = Color.Black)
                }
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.filterDayForecast(selectedDate.value)
                    }
                    showDateDialog.value = false
                }) {
                    Text(text = stringResource(R.string.text_confirm), color = Color.Black)
                }

            },
            dismissButton = {
                TextButton(onClick = { showDateDialog.value = false }) {
                    Text(text = stringResource(R.string.text_cancel), color = Color.Black)
                }
            },
            content = {
                DatePicker(
                    state = datePickerState,
                    colors = colors(
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
                            Text(stringResource(R.string.text_select_data), fontSize = 18.sp)
                        }
                    },
                )
            },
        )
    }
}

@Composable
private fun DialogDetails(
    showDialogDetails: MutableState<Boolean>,
    selectedItem: ListForecastUiState?
) {
    val scrollStateDetails = rememberScrollState()
    if (showDialogDetails.value) {
        Dialog(
            onDismissRequest = { showDialogDetails.value = false },
            content = {
                Card(
                    modifier = Modifier
                        .fillMaxSize(0.95f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = Color.White)

                    ) {
                        selectedItem?.let { item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    GlideImage(
                                        modifier = Modifier.fillMaxSize(0.8f),
                                        alignment = Alignment.TopCenter,
                                        model = "${ApiEndpoint.BASE_ENDPOINT_IMAGE}${
                                            item.weatherForecastUiState?.get(0)?.forecastWeatherIcon
                                        }@4x.png",
                                        contentDescription = null

                                    )
                                    ItemDetailsTempMinAndMax(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(
                                                end = 138.dp,
                                                top = 46.dp
                                            ),
                                        item.mainForecastUiState?.forecastMainTempMax.toString(),
                                        image = painterResource(id = R.drawable.icon_thermometer_up)
                                    )
                                    ItemDetailsTempMinAndMax(
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .padding(
                                                start = 132.dp,
                                                top = 172.dp
                                            ),
                                        item.mainForecastUiState?.forecastMainTempMin.toString(),
                                        image = painterResource(id = R.drawable.icon_thermometer_down)
                                    )
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(top = 204.dp)
                                            .verticalScroll(scrollStateDetails),
                                    ) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        DetailsDataAndHour(item)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        ItemDescriptionDetails(
                                            item = item.weatherForecastUiState?.get(0)?.forecastWeatherDescription.toString()
                                                .replaceFirstChar { it.uppercase() },
                                            titleDescriptionItem = stringResource(R.string.title_description)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        ItemDescriptionDetails(
                                            item = item.mainForecastUiState?.forecastMainPressure.toString(),
                                            titleDescriptionItem = stringResource(R.string.title_pressure_atmospheric),
                                            unitOfMeasurement = stringResource(R.string.uni_med_atm_press)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        ItemDescriptionDetails(
                                            item = item.mainForecastUiState?.forecastMainHumidity.toString(),
                                            titleDescriptionItem = stringResource(R.string.title_air_humidity),
                                            unitOfMeasurement = stringResource(R.string.percent)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        ItemDescriptionDetails(
                                            item = item.cloudsForecastUiState?.forecastCloudsAll.toString(),
                                            titleDescriptionItem = stringResource(R.string.title_cloudiness),
                                            unitOfMeasurement = stringResource(R.string.percent)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        ItemDescriptionDetails(
                                            item = item.windForecastUiState?.forecastWindSpeed.toString(),
                                            titleDescriptionItem = stringResource(R.string.title_wind_speed),
                                            unitOfMeasurement = stringResource(R.string.uni_med_veloc_km_h)
                                        )
                                    }

                                }
                            }
                        }

                    }
                }

            }
        )
    }
}

@Composable
private fun DetailsDataAndHour(item: ListForecastUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(),
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
                text = item.dataForecastUiState.toString(),
                fontFamily = openSansFontFamily,
                fontSize = 16.sp
            )
        }

        Row(
            modifier = Modifier.wrapContentWidth(),
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
                text = item.hourForecastUiState.toString(),
                fontFamily = openSansFontFamily,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun CreateRememberDatePicker() {
    rememberDatePickerState(selectableDates = object : SelectableDates {
        val zoneId = TimeZone.getTimeZone(Formats.TIME_ZONE).toZoneId()
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
private fun PermissionAlertDialog(
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