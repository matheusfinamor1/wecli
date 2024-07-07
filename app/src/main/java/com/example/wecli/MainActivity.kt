package com.example.wecli

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wecli.ui.theme.WecliTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WecliTheme {
                val splashScreen = installSplashScreen().apply {
                    setOnExitAnimationListener { viewProvider ->
                        ObjectAnimator.ofFloat(
                            viewProvider.view,
                            "scaleX",
                            0.5f, 0f
                        ).apply {
                            interpolator = OvershootInterpolator()
                            duration = 300
                            doOnEnd { viewProvider.remove() }
                            start()
                        }
                        ObjectAnimator.ofFloat(
                            viewProvider.view,
                            "scaleY",
                            0.5f, 0f
                        ).apply {
                            interpolator = OvershootInterpolator()
                            duration = 300
                            doOnEnd { viewProvider.remove() }
                            start()
                        }
                    }
                }
                val viewmodel: SplashScreenViewModel = koinViewModel()
                Text("Olaaa")
                splashScreen.setKeepOnScreenCondition{
                    viewmodel.splashShowValue.value
                }
            }
        }
    }
}
