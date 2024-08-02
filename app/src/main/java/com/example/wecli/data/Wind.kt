package com.example.wecli.data

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)
