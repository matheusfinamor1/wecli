package com.example.wecli.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.wecli.R
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
object SplashScreenRoute

@Composable
fun SplashScreen(
    onNavigateWeatherScreen: () -> Unit
) {
    val imageList = listOf(
        R.drawable.sun,
        R.drawable.cloudiness_day,
        R.drawable.cloudiness_night
    )
    var currentImageIndex by remember { mutableIntStateOf(0) }
    val transitionState = rememberInfiniteTransition(label = "")

    /** Define a animação para as imagens */
    val alpha by transitionState.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val scale by transitionState.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )


    /** Atualiza o índice da imagem a cada intervalo de tempo */
    LaunchedEffect(Unit) {
        while (true) {
            delay(2700)
            currentImageIndex = (currentImageIndex + 1) % imageList.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageList[currentImageIndex]),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(0.5f)
                .graphicsLayer(
                    alpha = alpha,
                    scaleX = scale,
                    scaleY = scale
                )
        )
    }

    /** Tempo da SplashScreen na tela e navegação para a tela principal */
    LaunchedEffect(Unit) {
        delay(6000)
        onNavigateWeatherScreen()
    }
}