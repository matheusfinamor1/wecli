package com.example.wecli.uiLayer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.wecli.uiLayer.ui.theme.White
import com.example.wecli.uiLayer.ui.theme.openSansFontFamily

@Composable
fun ItemContentWeatherCurrent(
    background: Color,
    dataFirstLine: Triple<Int?, Int?, String?>,
    dataSecondLine: Triple<Int?, Int?, String?>,
    modifier: Modifier = Modifier
) {
    val (iconFirstLine, valueFirstLine, unitFirstLine) = dataFirstLine

    val (iconSecondLine, valueSecondLine, unitSecondLine) = dataSecondLine

    Column(
        modifier = modifier
            .padding(2.dp)
            .border(
                color = Color.LightGray, shape = ShapeDefaults.Small, width = 1.dp
            )
            .clip(RoundedCornerShape(3.dp))
            .background(color = background.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconFirstLine?.let {
                Image(
                    painter = painterResource(id = iconFirstLine),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 8.dp)
                )
            }

            valueFirstLine?.let {
                Text(
                    text = "$it $unitFirstLine",
                    fontFamily = openSansFontFamily,
                    color = White
                )
            }
        }

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconSecondLine?.let {
                Image(
                    painter = painterResource(id = iconSecondLine),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 8.dp)
                )
            }

            valueSecondLine?.let {
                Text(
                    text = "$it$unitSecondLine",
                    fontFamily = openSansFontFamily,
                    color = White
                )
            }
        }
    }

}