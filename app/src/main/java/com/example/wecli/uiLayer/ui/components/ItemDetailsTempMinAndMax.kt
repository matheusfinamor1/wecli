package com.example.wecli.uiLayer.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.wecli.uiLayer.ui.theme.openSansFontFamily

@Composable
fun ItemDetailsTempMinAndMax(
    modifier: Modifier = Modifier,
    item: String,
    image: Painter
) {
    Row(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = image,
            contentDescription = null
        )
        Text(
            text = item,
            fontFamily = openSansFontFamily,
        )
    }
}