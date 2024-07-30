package com.example.wecli.data

import kotlinx.serialization.Serializable

@Serializable
data class Sys(
    val country: String? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null
)
