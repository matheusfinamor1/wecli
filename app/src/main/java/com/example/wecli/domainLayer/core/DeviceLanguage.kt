package com.example.wecli.domainLayer.core

import java.util.Locale

class DeviceLanguage {
    private val deviceLanguage = Locale.getDefault().language

    fun getDeviceLanguage(): String {
        return when (deviceLanguage) {
            "pt" -> "pt_br"
            else -> {"en"}
        }
    }
}