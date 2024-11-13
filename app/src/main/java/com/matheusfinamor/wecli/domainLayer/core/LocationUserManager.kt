package com.matheusfinamor.wecli.domainLayer.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationUserManager(
    private val context: Context,
) {
    @Composable
    fun RequestPermission(
        context: Context,
        showPermissionRequest: MutableState<Boolean>,
        fusedLocationClient: FusedLocationProviderClient,
        onGetCurrentLocationSuccess: (Double, Double) -> Unit,
        onGetCurrentLocationFailure: (Exception) -> Unit
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
                            onGetCurrentLocationSuccess(it.first, it.second)

                        },
                        onGetCurrentLocationFailure = {
                            onGetCurrentLocationFailure(it)
                        }
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
                                onGetCurrentLocationSuccess(it.first, it.second)
                            },
                            onGetCurrentLocationFailure = {
                                onGetCurrentLocationFailure(it)
                            }
                        )
                }

        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(
        fusedLocationClient: FusedLocationProviderClient,
        onGetCurrentLocationSuccess: (Pair<Double, Double>) -> Unit,
        onGetCurrentLocationFailure: (Exception) -> Unit,
        priority: Boolean = true
    ) {
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY
        if (areLocationPermissionsGranted()) {
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

    private fun areLocationPermissionsGranted(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }
}