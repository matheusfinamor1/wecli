package com.matheusfinamor.wecli.uiLayer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.matheusfinamor.wecli.uiLayer.ui.theme.White
import com.matheusfinamor.wecli.uiLayer.ui.theme.openSansFontFamily

@Composable
fun ItemContentForecastTempMaxAndMin(
    modifier: Modifier = Modifier,
    temp: String,
    image: Painter,
) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .padding(6.dp)
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = image,
            contentDescription = null
        )
        Text(
            text = temp,
            fontFamily = openSansFontFamily,
            color = White
        )
    }
}