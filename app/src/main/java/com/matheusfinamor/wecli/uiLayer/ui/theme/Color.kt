package com.matheusfinamor.wecli.uiLayer.ui.theme

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val PurpleGrey40 = Color(0xFF625b71)

val White = Color(0xFFFFFFFF)

val Blue = Color(0xFF6495eD)
val BlueToWhiteGradient = createShaderBrush(Blue, White)

val BrownAfternoon = Color(0xFFCD853F)
val BrownToWhiteGradient = createShaderBrush(BrownAfternoon, White)

val BlueNight = Color(0xFF2F4F4F)
val BlueNightToWhiteGradient = createShaderBrush(BlueNight, White)

val Gray = Color(0xFFA49894)

private fun createShaderBrush(color1: Color, color2: Color) = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val biggerDimension = maxOf(size.height, size.width)
        return RadialGradientShader(
            colors = listOf(color1, color2),
            center = size.center,
            radius = biggerDimension / 0.3f,
            colorStops = listOf(0f, 0.95f)
        )
    }
}