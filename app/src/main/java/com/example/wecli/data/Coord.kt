package com.example.wecli.data

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lon: Double,
    val lat: Double
)
