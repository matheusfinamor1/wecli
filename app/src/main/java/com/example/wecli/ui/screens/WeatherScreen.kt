package com.example.wecli.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

@Composable
fun WeatherScreen() {
    val context = LocalContext.current
    val showPermissionRequest = remember { mutableStateOf(false) }
    RequestPermission(context, showPermissionRequest)
    ShowDialog(showPermissionRequest, context)
    Column {
        Text("Ola")
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
    showPermissionRequest: MutableState<Boolean>
) {
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted.not()) {
                showPermissionRequest.value = true
            }
        }
    )
    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            .let { isGranted ->
                if (isGranted == PackageManager.PERMISSION_DENIED)
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
    }
}
